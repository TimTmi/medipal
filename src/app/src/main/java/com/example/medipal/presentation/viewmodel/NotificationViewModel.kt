package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.*
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.AddMedicationDoseUseCase
import com.example.medipal.domain.usecase.GetMedicationDoseUseCase
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

            val profileId = profileRepositoryManager.getCurrentProfileId()
            
            combine(
                getMedicationsUseCase(profileId),
                getAppointmentsUseCase(profileId),
                getRemindersUseCase(profileId),
                getMedicationDoseUseCase(profileId)
            ) { medications, appointments, reminders, medicationDoses ->
                val allNotifications = mutableListOf<NotificationItem>()
                val currentTime = System.currentTimeMillis()
                val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault())
                
                // Debug logging
                println("DEBUG: Loading notifications - Medications: ${medications.size}, Appointments: ${appointments.size}, Reminders: ${reminders.size}")
                
                // Convert medications to notifications
                medications.forEach { medication ->
                    // Check if there's a dose record for this medication at this time
                    val doseRecord = medicationDoses.find { 
                        it.medicationId == medication.id && it.scheduledTime == medication.scheduleTime 
                    }
                    
                    val status = when {
                        doseRecord != null -> when (doseRecord.status) {
                            DoseStatus.TAKEN -> NotificationStatus.TAKEN
                            DoseStatus.SKIPPED -> NotificationStatus.SKIPPED
                            DoseStatus.MISSED -> NotificationStatus.MISSED
                        }
                        else -> determineStatus(medication.scheduleTime, currentTime)
                    }
                    
                    val time = Instant.ofEpochMilli(medication.scheduleTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                        .format(timeFormatter)
                    
                    println("DEBUG: Medication - ${medication.name}, scheduleTime: ${medication.scheduleTime}, status: $status")
                    
                    allNotifications.add(
                        NotificationItem(
                            id = "med_${medication.id}",
                            title = "${medication.name}",
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
                
                // Convert appointments to notifications
                appointments.forEach { appointment ->
                    val status = determineStatus(appointment.dateTime, currentTime)
                    val time = Instant.ofEpochMilli(appointment.dateTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                        .format(timeFormatter)
                    
                    println("DEBUG: Appointment - ${appointment.title}, scheduleTime: ${appointment.dateTime}, status: $status")
                    
                    allNotifications.add(
                        NotificationItem(
                            id = "apt_${appointment.id}",
                            title = appointment.title,
                            subtitle = "Health Check Up",
                            time = time,
                            scheduleTime = appointment.dateTime,
                            status = status,
                            type = NotificationType.APPOINTMENT,
                            doctorName = appointment.doctorName,
                            originalItem = appointment
                        )
                    )
                }
                
                // Convert reminders to notifications
                reminders.forEach { reminder ->
                    val status = determineStatus(reminder.dateTime, currentTime)
                    val time = Instant.ofEpochMilli(reminder.dateTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalTime()
                        .format(timeFormatter)
                    
                    println("DEBUG: Reminder - ${reminder.title}, scheduleTime: ${reminder.dateTime}, status: $status")
                    
                    allNotifications.add(
                        NotificationItem(
                            id = "rem_${reminder.id}",
                            title = reminder.title,
                            subtitle = "Reminder",
                            time = time,
                            scheduleTime = reminder.dateTime,
                            status = status,
                            type = NotificationType.REMINDER,
                            originalItem = reminder
                        )
                    )
                }
                
                // Filter notifications for today and yesterday
                val today = LocalDate.now()
                val yesterday = today.minusDays(1)
                
                println("DEBUG: Today: $today, Yesterday: $yesterday")
                
                val todayNotifications = allNotifications.filter { notification ->
                    val notificationDate = Instant.ofEpochMilli(notification.scheduleTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    val isToday = notificationDate == today
                    println("DEBUG: ${notification.title} - Date: $notificationDate, IsToday: $isToday")
                    isToday
                }.sortedBy { it.scheduleTime }
                
                val yesterdayNotifications = allNotifications.filter { notification ->
                    val notificationDate = Instant.ofEpochMilli(notification.scheduleTime)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    val isYesterday = notificationDate == yesterday
                    println("DEBUG: ${notification.title} - Date: $notificationDate, IsYesterday: $isYesterday")
                    isYesterday
                }.sortedBy { it.scheduleTime }
                
                println("DEBUG: Final - Today: ${todayNotifications.size}, Yesterday: ${yesterdayNotifications.size}")
                
                NotificationUiState(
                    todayNotifications = todayNotifications,
                    yesterdayNotifications = yesterdayNotifications,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
                updateGlobalNotifications(newState.todayNotifications + newState.yesterdayNotifications)
            }
        }
    }

    private fun determineStatus(scheduleTime: Long, currentTime: Long): NotificationStatus {
        return if (currentTime > scheduleTime) {
            NotificationStatus.MISSED
        } else {
            NotificationStatus.UPCOMING
        }
    }

    fun getNotificationById(id: String): NotificationItem? {
        val currentState = _uiState.value
        val localNotification = (currentState.todayNotifications + currentState.yesterdayNotifications)
            .find { it.id == id }
        return localNotification ?: getGlobalNotificationById(id)
    }

    fun markAsTaken(notificationId: String) {
        viewModelScope.launch {
            try {
                val notification = getNotificationById(notificationId)
                if (notification?.type == NotificationType.MEDICATION) {
                    val medicationId = notificationId.removePrefix("med_")
                    val profileId = profileRepositoryManager.getCurrentProfileId()
                    
                    // Check if dose record already exists
                    val existingDose = getMedicationDoseUseCase.getByMedicationAndTime(
                        medicationId, notification.scheduleTime
                    )
                    
                    if (existingDose != null) {
                        // Update existing dose record
                        val updatedDose = existingDose.copy(
                            status = DoseStatus.TAKEN,
                            actualTime = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        addMedicationDoseUseCase(updatedDose)
                    } else {
                        // Create new dose record
                        val newDose = MedicationDose(
                            medicationId = medicationId,
                            profileId = profileId,
                            scheduledTime = notification.scheduleTime,
                            actualTime = System.currentTimeMillis(),
                            status = DoseStatus.TAKEN
                        )
                        addMedicationDoseUseCase(newDose)
                    }
                    
                    // Reload notifications to reflect the change
                    loadNotifications()
                }
            } catch (e: Exception) {
                println("Error marking as taken: ${e.message}")
            }
        }
    }

    fun markAsSkipped(notificationId: String) {
        viewModelScope.launch {
            try {
                val notification = getNotificationById(notificationId)
                if (notification?.type == NotificationType.MEDICATION) {
                    val medicationId = notificationId.removePrefix("med_")
                    val profileId = profileRepositoryManager.getCurrentProfileId()
                    
                    // Check if dose record already exists
                    val existingDose = getMedicationDoseUseCase.getByMedicationAndTime(
                        medicationId, notification.scheduleTime
                    )
                    
                    if (existingDose != null) {
                        // Update existing dose record
                        val updatedDose = existingDose.copy(
                            status = DoseStatus.SKIPPED,
                            actualTime = System.currentTimeMillis(),
                            updatedAt = System.currentTimeMillis()
                        )
                        addMedicationDoseUseCase(updatedDose)
                    } else {
                        // Create new dose record
                        val newDose = MedicationDose(
                            medicationId = medicationId,
                            profileId = profileId,
                            scheduledTime = notification.scheduleTime,
                            actualTime = System.currentTimeMillis(),
                            status = DoseStatus.SKIPPED
                        )
                        addMedicationDoseUseCase(newDose)
                    }
                    
                    // Reload notifications to reflect the change
                    loadNotifications()
                }
            } catch (e: Exception) {
                println("Error marking as skipped: ${e.message}")
            }
        }
    }
}
