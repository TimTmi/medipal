package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.*
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.AddMedicationDoseUseCase
import com.example.medipal.domain.usecase.GetMedicationDoseUseCase
import com.example.medipal.domain.usecase.UpdateAppointmentUseCase
import com.example.medipal.domain.usecase.UpdateReminderUseCase
import com.example.medipal.domain.usecase.AddAppointmentStatusUseCase
import com.example.medipal.domain.usecase.AddReminderStatusUseCase
import com.example.medipal.domain.usecase.GetAppointmentStatusUseCase
import com.example.medipal.domain.usecase.GetReminderStatusUseCase
import com.example.medipal.domain.usecase.UpdateAppointmentStatusUseCase
import com.example.medipal.domain.usecase.UpdateReminderStatusUseCase
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class NotificationUiState(
    val todayNotifications: List<NotificationItem> = emptyList(),
    val yesterdayNotifications: List<NotificationItem> = emptyList(),
    val isLoading: Boolean = false
)

class NotificationViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val addMedicationDoseUseCase: AddMedicationDoseUseCase,
    private val getMedicationDoseUseCase: GetMedicationDoseUseCase,
    private val updateAppointmentUseCase: UpdateAppointmentUseCase,
    private val updateReminderUseCase: UpdateReminderUseCase,
    private val addAppointmentStatusUseCase: AddAppointmentStatusUseCase,
    private val updateAppointmentStatusUseCase: UpdateAppointmentStatusUseCase,
    private val getAppointmentStatusUseCase: GetAppointmentStatusUseCase,
    private val addReminderStatusUseCase: AddReminderStatusUseCase,
    private val updateReminderStatusUseCase: UpdateReminderStatusUseCase,
    private val getReminderStatusUseCase: GetReminderStatusUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    companion object {
        private var globalNotifications: List<NotificationItem> = emptyList()

        fun getGlobalNotificationById(id: String): NotificationItem? {
            return globalNotifications.find { it.id == id }
        }

        private fun updateGlobalNotifications(notifications: List<NotificationItem>) {
            globalNotifications = notifications
        }
    }

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            profileRepositoryManager.currentProfileId
                .flatMapLatest { profileId ->
                    combine(
                        getMedicationsUseCase(profileId),
                        getAppointmentsUseCase(profileId),
                        getRemindersUseCase(profileId),
                        getMedicationDoseUseCase(profileId),
                        getAppointmentStatusUseCase.getAll(),
                        getReminderStatusUseCase.getAll()
                    ) { flows: Array<Any> ->
                        val medications = flows[0] as List<Medication>
                        val appointments = flows[1] as List<Appointment>
                        val reminders = flows[2] as List<Reminder>
                        val medicationDoses = flows[3] as List<MedicationDose>
                        val appointmentStatuses = flows[4] as List<AppointmentStatus>
                        val reminderStatuses = flows[5] as List<ReminderStatus>

                        val allNotifications = mutableListOf<NotificationItem>()
                        val currentTime = System.currentTimeMillis()
                        val timeFormatter =
                            DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())

                        // Build notifications
                        medications.forEach { medication ->
                            val doseRecord = medicationDoses.find {
                                it.medicationId == medication.id && it.scheduledTime == medication.scheduleTime
                            }
                            val status = doseRecord?.let {
                                when (it.status) {
                                    DoseStatus.TAKEN -> NotificationStatus.TAKEN
                                    DoseStatus.SKIPPED -> NotificationStatus.SKIPPED
                                    DoseStatus.MISSED -> NotificationStatus.MISSED
                                }
                            } ?: determineStatus(medication.scheduleTime, currentTime)

                            val time = Instant.ofEpochMilli(medication.scheduleTime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalTime()
                                .format(timeFormatter)

                            allNotifications.add(
                                NotificationItem(
                                    id = "med_${medication.id}",
                                    title = medication.name,
                                    subtitle = "Dose",
                                    time = time,
                                    scheduleTime = medication.scheduleTime,
                                    status = status,
                                    type = NotificationType.MEDICATION,
                                    instructions = medication.description.ifEmpty { "Frequency: ${medication.frequency.displayText}" },
                                    originalItem = medication
                                )
                            )
                        }

                        appointments.forEach { appointment ->
                            val existingStatus =
                                appointmentStatuses.find { it.appointmentId == appointment.id && it.scheduledTime == appointment.dateTime }
                            val status = when (existingStatus?.status) {
                                AppointmentStatusType.ATTENDED -> NotificationStatus.TAKEN
                                AppointmentStatusType.MISSED -> NotificationStatus.SKIPPED
                                else -> determineStatus(appointment.dateTime, currentTime)
                            }
                            val subtitle = when (status) {
                                NotificationStatus.TAKEN -> "Attended"
                                NotificationStatus.SKIPPED -> "Missed"
                                NotificationStatus.MISSED -> "Missed"
                                NotificationStatus.UPCOMING -> "Upcoming"
                            }

                            val time = Instant.ofEpochMilli(appointment.dateTime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalTime()
                                .format(timeFormatter)

                            allNotifications.add(
                                NotificationItem(
                                    id = "apt_${appointment.id}",
                                    title = appointment.title,
                                    subtitle = subtitle,
                                    time = time,
                                    scheduleTime = appointment.dateTime,
                                    status = status,
                                    type = NotificationType.APPOINTMENT,
                                    doctorName = appointment.doctorName,
                                    originalItem = appointment
                                )
                            )
                        }

                        reminders.forEach { reminder ->
                            val existingStatus =
                                reminderStatuses.find { it.reminderId == reminder.id && it.scheduledTime == reminder.dateTime }
                            val status = when (existingStatus?.status) {
                                ReminderStatusType.COMPLETED -> NotificationStatus.TAKEN
                                ReminderStatusType.MISSED, ReminderStatusType.SKIPPED -> NotificationStatus.SKIPPED
                                else -> determineStatus(reminder.dateTime, currentTime)
                            }
                            val subtitle = when (status) {
                                NotificationStatus.TAKEN -> "Completed"
                                NotificationStatus.SKIPPED -> "Skipped"
                                NotificationStatus.MISSED -> "Overdue"
                                NotificationStatus.UPCOMING -> "Pending"
                            }

                            val time = Instant.ofEpochMilli(reminder.dateTime)
                                .atZone(ZoneId.systemDefault())
                                .toLocalTime()
                                .format(timeFormatter)

                            allNotifications.add(
                                NotificationItem(
                                    id = "rem_${reminder.id}",
                                    title = reminder.title,
                                    subtitle = subtitle,
                                    time = time,
                                    scheduleTime = reminder.dateTime,
                                    status = status,
                                    type = NotificationType.REMINDER,
                                    originalItem = reminder
                                )
                            )
                        }

                        val today = LocalDate.now()
                        val yesterday = today.minusDays(1)

                        val todayNotifications = allNotifications.filter {
                            Instant.ofEpochMilli(it.scheduleTime).atZone(ZoneId.systemDefault()).toLocalDate() == today
                        }.sortedBy { it.scheduleTime }

                        val yesterdayNotifications = allNotifications.filter {
                            Instant.ofEpochMilli(it.scheduleTime).atZone(ZoneId.systemDefault()).toLocalDate() == yesterday
                        }.sortedBy { it.scheduleTime }

                        NotificationUiState(
                            todayNotifications = todayNotifications,
                            yesterdayNotifications = yesterdayNotifications,
                            isLoading = false
                        )
                    }
                }
                .collect { newState ->
                    _uiState.value = newState
                    updateGlobalNotifications(newState.todayNotifications + newState.yesterdayNotifications)
                }
        }
    }

    private fun determineStatus(scheduleTime: Long, currentTime: Long): NotificationStatus =
        if (currentTime > scheduleTime) NotificationStatus.MISSED else NotificationStatus.UPCOMING

    fun getNotificationById(id: String): NotificationItem? {
        val localNotification =
            (_uiState.value.todayNotifications + _uiState.value.yesterdayNotifications).find { it.id == id }
        return localNotification ?: getGlobalNotificationById(id)
    }

    fun markAsTaken(notificationId: String) {
        viewModelScope.launch {
            try {
                println("DEBUG: markAsTaken called with notificationId: $notificationId")
                val notification = getNotificationById(notificationId)
                println("DEBUG: Found notification: ${notification?.title}, type: ${notification?.type}")

                when (notification?.type) {
                    NotificationType.MEDICATION -> {
                        val medicationId = notificationId.removePrefix("med_")
                        val profileId = profileRepositoryManager.getCurrentProfileId()
                        println("DEBUG: medicationId: $medicationId, profileId: $profileId")
                        println("DEBUG: scheduleTime: ${notification.scheduleTime}")

                        val existingDose = getMedicationDoseUseCase.getByMedicationAndTime(
                            medicationId, notification.scheduleTime
                        )
                        println("DEBUG: existingDose: $existingDose")

                        if (existingDose != null) {
                            val updatedDose = existingDose.copy(
                                status = DoseStatus.TAKEN,
                                actualTime = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            println("DEBUG: Updating existing dose: $updatedDose")
                            addMedicationDoseUseCase(updatedDose)
                        } else {
                            val newDose = MedicationDose(
                                medicationId = medicationId,
                                profileId = profileId,
                                scheduledTime = notification.scheduleTime,
                                actualTime = System.currentTimeMillis(),
                                status = DoseStatus.TAKEN
                            )
                            println("DEBUG: Creating new dose: $newDose")
                            addMedicationDoseUseCase(newDose)
                        }
                    }

                    NotificationType.APPOINTMENT -> {
                        val appointmentId = notificationId.removePrefix("apt_")
                        println("DEBUG: Marking appointment as attended: $appointmentId")

                        if (notification != null) {
                            val profileId = profileRepositoryManager.getCurrentProfileId()
                            val existingStatus = getAppointmentStatusUseCase.getByAppointmentAndTime(
                                appointmentId, notification.scheduleTime
                            )
                            println("DEBUG: existingStatus: $existingStatus")

                            if (existingStatus != null) {
                                val updatedStatus = existingStatus.copy(
                                    status = AppointmentStatusType.ATTENDED,
                                    actualTime = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                println("DEBUG: Updating existing appointment status: $updatedStatus")
                                updateAppointmentStatusUseCase(updatedStatus)
                            } else {
                                val appointmentStatus = AppointmentStatus(
                                    appointmentId = appointmentId,
                                    profileId = profileId,
                                    scheduledTime = notification.scheduleTime,
                                    actualTime = System.currentTimeMillis(),
                                    status = AppointmentStatusType.ATTENDED
                                )
                                println("DEBUG: Creating new appointment status: $appointmentStatus")
                                addAppointmentStatusUseCase(appointmentStatus)
                            }
                        }
                    }

                    NotificationType.REMINDER -> {
                        val reminderId = notificationId.removePrefix("rem_")
                        println("DEBUG: Marking reminder as completed: $reminderId")

                        if (notification != null) {
                            val profileId = profileRepositoryManager.getCurrentProfileId()
                            val existingStatus = getReminderStatusUseCase.getByReminderAndTime(
                                reminderId, notification.scheduleTime
                            )
                            println("DEBUG: existingStatus: $existingStatus")

                            if (existingStatus != null) {
                                val updatedStatus = existingStatus.copy(
                                    status = ReminderStatusType.COMPLETED,
                                    actualTime = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                println("DEBUG: Updating existing reminder status: $updatedStatus")
                                updateReminderStatusUseCase(updatedStatus)
                            } else {
                                val reminderStatus = ReminderStatus(
                                    reminderId = reminderId,
                                    profileId = profileId,
                                    scheduledTime = notification.scheduleTime,
                                    actualTime = System.currentTimeMillis(),
                                    status = ReminderStatusType.COMPLETED
                                )
                                println("DEBUG: Creating new reminder status: $reminderStatus")
                                addReminderStatusUseCase(reminderStatus)
                            }
                        }
                    }

                    else -> println("DEBUG: Unknown notification type: ${notification?.type}")
                }

                println("DEBUG: Calling loadNotifications() to refresh")
                loadNotifications()

            } catch (e: Exception) {
                println("Error marking as taken: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun markAsSkipped(notificationId: String) {
        viewModelScope.launch {
            try {
                println("DEBUG: markAsSkipped called with notificationId: $notificationId")
                val notification = getNotificationById(notificationId)
                println("DEBUG: Found notification: ${notification?.title}, type: ${notification?.type}")

                when (notification?.type) {
                    NotificationType.MEDICATION -> {
                        val medicationId = notificationId.removePrefix("med_")
                        val profileId = profileRepositoryManager.getCurrentProfileId()
                        println("DEBUG: medicationId: $medicationId, profileId: $profileId")
                        println("DEBUG: scheduleTime: ${notification.scheduleTime}")

                        val existingDose = getMedicationDoseUseCase.getByMedicationAndTime(
                            medicationId, notification.scheduleTime
                        )
                        println("DEBUG: existingDose: $existingDose")

                        if (existingDose != null) {
                            val updatedDose = existingDose.copy(
                                status = DoseStatus.SKIPPED,
                                actualTime = System.currentTimeMillis(),
                                updatedAt = System.currentTimeMillis()
                            )
                            println("DEBUG: Updating existing dose: $updatedDose")
                            addMedicationDoseUseCase(updatedDose)
                        } else {
                            val newDose = MedicationDose(
                                medicationId = medicationId,
                                profileId = profileId,
                                scheduledTime = notification.scheduleTime,
                                actualTime = System.currentTimeMillis(),
                                status = DoseStatus.SKIPPED
                            )
                            println("DEBUG: Creating new dose: $newDose")
                            addMedicationDoseUseCase(newDose)
                        }
                    }

                    NotificationType.APPOINTMENT -> {
                        val appointmentId = notificationId.removePrefix("apt_")
                        println("DEBUG: Marking appointment as missed: $appointmentId")

                        val appointment = notification.originalItem as? Appointment
                        if (appointment != null) {
                            val profileId = profileRepositoryManager.getCurrentProfileId()
                            val existingStatus = getAppointmentStatusUseCase.getByAppointmentAndTime(
                                appointmentId, notification.scheduleTime
                            )

                            if (existingStatus != null) {
                                val appointmentStatus = existingStatus.copy(
                                    status = AppointmentStatusType.MISSED,
                                    actualTime = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                println("DEBUG: Updating appointment status: $appointmentStatus")
                                updateAppointmentStatusUseCase(appointmentStatus)
                            } else {
                                val appointmentStatus = AppointmentStatus(
                                    appointmentId = appointmentId,
                                    profileId = profileId,
                                    scheduledTime = notification.scheduleTime,
                                    actualTime = System.currentTimeMillis(),
                                    status = AppointmentStatusType.MISSED
                                )
                                println("DEBUG: Creating appointment status: $appointmentStatus")
                                addAppointmentStatusUseCase(appointmentStatus)
                            }
                        }
                    }

                    NotificationType.REMINDER -> {
                        val reminderId = notificationId.removePrefix("rem_")
                        println("DEBUG: Marking reminder as missed: $reminderId")

                        val reminder = notification.originalItem as? Reminder
                        if (reminder != null) {
                            val profileId = profileRepositoryManager.getCurrentProfileId()
                            val existingStatus = getReminderStatusUseCase.getByReminderAndTime(
                                reminderId, reminder.dateTime
                            )

                            if (existingStatus != null) {
                                val reminderStatus = existingStatus.copy(
                                    status = ReminderStatusType.SKIPPED,
                                    actualTime = System.currentTimeMillis(),
                                    updatedAt = System.currentTimeMillis()
                                )
                                println("DEBUG: Updating reminder status: $reminderStatus")
                                updateReminderStatusUseCase(reminderStatus)
                            } else {
                                val reminderStatus = ReminderStatus(
                                    reminderId = reminderId,
                                    profileId = profileId,
                                    scheduledTime = reminder.dateTime,
                                    actualTime = System.currentTimeMillis(),
                                    status = ReminderStatusType.SKIPPED
                                )
                                println("DEBUG: Creating reminder status: $reminderStatus")
                                addReminderStatusUseCase(reminderStatus)
                            }
                        }
                    }

                    else -> println("DEBUG: Unknown notification type: ${notification?.type}")
                }

                println("DEBUG: Calling loadNotifications() to refresh")
                loadNotifications()

            } catch (e: Exception) {
                println("Error marking as skipped: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}
