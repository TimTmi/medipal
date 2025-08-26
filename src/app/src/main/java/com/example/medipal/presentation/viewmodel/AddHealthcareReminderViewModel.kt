package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Reminder
//import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.model.Frequency
import com.example.medipal.domain.usecase.AddReminderUseCase
import com.example.medipal.domain.service.NotificationService
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.time.DayOfWeek
import java.text.SimpleDateFormat
import java.util.Locale

class AddHealthcareReminderViewModel(
    private val addReminderUseCase: AddReminderUseCase,
    private val notificationService: NotificationService,
    private val profileRepositoryManager: ProfileRepositoryManager
//    private val historyRepository: HistoryRepository
) : ViewModel() {

    // Sử dụng profile hiện tại động thay vì cố định

    // --- CÁC TRẠNG THÁI CHO LUỒNG THÊM LỜI NHẮC ---
    val reminderCategories = mapOf(
        "Mental Health" to listOf("Meditation", "Breathing exercise", "Break reminder"),
        "Vital & Health monitoring" to listOf("Blood pressure", "Blood glucose", "Body temperature"),
        "Exercise" to listOf("Walking", "Yoga", "Running")
    )
    val selectedCategory = MutableStateFlow("")
    val selectedActivity = MutableStateFlow("")
    val selectedTime = MutableStateFlow("16:46 AM")
    val sessionCount = MutableStateFlow(1)

    val baseFrequencyOptions = listOf(
        "Every day", "Only as needed", "Every X days",
        "Specific days of the week", "Every X weeks"
    )
    val selectedFrequencyObject = MutableStateFlow<Frequency>(Frequency.EveryDay())
    val xDaysValue = MutableStateFlow(2)
    val selectedWeekDays = MutableStateFlow<List<DayOfWeek>>(emptyList())
    val xWeeksValue = MutableStateFlow(1)



    fun setFrequencyEveryDay() {
        selectedFrequencyObject.value = Frequency.EveryDay()
    }
    fun setFrequencyAsNeeded() {
        selectedFrequencyObject.value = Frequency.AsNeeded()
    }
    fun saveFrequencyXDays(days: Int) {
        xDaysValue.value = days
        selectedFrequencyObject.value = Frequency.EveryXDays(days)
    }
    fun saveFrequencySpecificDays(days: List<DayOfWeek>) {
        selectedWeekDays.value = days
        selectedFrequencyObject.value = Frequency.SpecificDaysOfWeek(days)
    }
    fun saveFrequencyXWeeks(weeks: Int, days: List<DayOfWeek>) {
        xWeeksValue.value = weeks
        selectedWeekDays.value = days
        selectedFrequencyObject.value = Frequency.EveryXWeeks(weeks, days)
    }

    // --- CÁC HÀM CẬP NHẬT TRẠNG THÁI TỪ UI ---
    fun onCategorySelected(category: String) {
        selectedCategory.value = category
        selectedActivity.value = reminderCategories[category]?.firstOrNull() ?: ""
    }
    fun onActivitySelected(activity: String) {
        selectedActivity.value = activity
    }
    fun onSessionCountChanged(count: Int) {
        // Đảm bảo số phiên không nhỏ hơn 1
        if (count >= 1) {
            sessionCount.value = count
        }
    }
    fun onTimeSelected(time: String) {
        selectedTime.value = time
    }

    // --- LOGIC CHO DIALOG THÀNH CÔNG ---
    private val _lastSavedReminderTitle = MutableStateFlow<String?>(null)
    val lastSavedReminderTitle = _lastSavedReminderTitle.asStateFlow()

    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()

    // --- HÀM LƯU VÀ ĐÓNG DIALOG ---
    fun saveReminder() {
        viewModelScope.launch {
            val activityToSave = selectedActivity.value

            // Convert user-selected time to timestamp with current date
            val timeString = selectedTime.value.ifEmpty { "09:00 AM" }
            val scheduledTimestamp = try {
                val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val parsedTime = format.parse(timeString)

                // Get current date and combine with selected time
                val calendar = Calendar.getInstance()
                val timeCalendar = Calendar.getInstance()
                timeCalendar.time = parsedTime ?: Date()

                // Set the time on current date
                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY))
                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE))
                calendar.set(Calendar.SECOND, 0)
                calendar.set(Calendar.MILLISECOND, 0)

                calendar.timeInMillis
            } catch (e: Exception) {
                System.currentTimeMillis()
            }

            val newReminder = Reminder(
                id = UUID.randomUUID().toString(),
                title = activityToSave,
                dateTime = scheduledTimestamp,
                description = "Sessions: ${sessionCount.value}"
            )
            // Gọi UseCase (là một suspend function) bên trong coroutine
            addReminderUseCase(newReminder, profileRepositoryManager.getCurrentProfileId())
            
            // Schedule notification
            notificationService.scheduleReminderNotification(newReminder)
            
            // Auto-add to history
//            historyRepository.addReminderHistory(newReminder)

            _lastSavedReminderTitle.value = activityToSave
            _showSuccessDialog.value = true
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedReminderTitle.value = null
        _showSuccessDialog.value = false
    }
} 