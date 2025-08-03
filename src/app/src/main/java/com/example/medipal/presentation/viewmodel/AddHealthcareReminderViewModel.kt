package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Reminder
//import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.usecase.AddReminderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.text.SimpleDateFormat
import java.util.Locale

class AddHealthcareReminderViewModel(
    private val addReminderUseCase: AddReminderUseCase,
//    private val historyRepository: HistoryRepository
) : ViewModel() {

    // --- CÁC TRẠNG THÁI CHO LUỒNG THÊM LỜI NHẮC ---
    val reminderCategories = mapOf(
        "Mental Health" to listOf("Meditation", "Breathing exercise", "Break reminder"),
        "Vital & Health monitoring" to listOf("Blood pressure", "Blood glucose", "Body temperature"),
        "Exercise" to listOf("Walking", "Yoga", "Running")
    )
    val frequencyOptions = listOf(
        "Every day", "Only as needed", "Every X days",
        "Specific days of the week", "Every X weeks"
    )
    val selectedFrequency = MutableStateFlow(frequencyOptions.first())
    val selectedCategory = MutableStateFlow("")
    val selectedActivity = MutableStateFlow("")
    val selectedTime = MutableStateFlow("16:46 AM")
    val sessionCount = MutableStateFlow(1)

    // --- CÁC HÀM CẬP NHẬT TRẠNG THÁI TỪ UI ---
    fun onCategorySelected(category: String) {
        selectedCategory.value = category
        selectedActivity.value = reminderCategories[category]?.firstOrNull() ?: ""
    }

    fun onActivitySelected(activity: String) {
        selectedActivity.value = activity
    }

    fun onFrequencySelected(frequency: String) {
        selectedFrequency.value = frequency
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
            
            // Convert user-selected time to timestamp
            val timeString = selectedTime.value.ifEmpty { "09:00 AM" }
            val scheduledTimestamp = try {
                val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                val parsedTime = format.parse(timeString)
                parsedTime?.time ?: System.currentTimeMillis()
            } catch (e: Exception) {
                System.currentTimeMillis()
            }

            val newReminder = Reminder(
                id = UUID.randomUUID().toString(),
                title = activityToSave,
                scheduleTime = scheduledTimestamp,
                notes = "Frequency: ${selectedFrequency.value}, Sessions: ${sessionCount.value}"
            )
            // Gọi UseCase (là một suspend function) bên trong coroutine
            addReminderUseCase(newReminder)
            
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