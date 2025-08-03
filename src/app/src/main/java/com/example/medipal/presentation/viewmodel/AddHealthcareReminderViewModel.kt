package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.usecase.AddHealthcareReminderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddHealthcareReminderViewModel(
    private val addHealthcareReminderUseCase: AddHealthcareReminderUseCase,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    // Các trạng thái cho từng bước
    val reminderTitle = MutableStateFlow("")
    val time = MutableStateFlow("16:46 AM") // Giả lập thời gian được chọn
    
    // Trạng thái cho dialog thành công
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()
    
    private val _lastSavedReminderTitle = MutableStateFlow<String?>(null)
    val lastSavedReminderTitle = _lastSavedReminderTitle.asStateFlow()

    // Method to update selected time
    fun updateTime(newTime: String) {
        time.value = newTime
    }

    fun saveHealthcareReminder() {
        viewModelScope.launch {
            val newReminder = ScheduledEvent.Reminder(
                id = UUID.randomUUID().toString(),
                title = reminderTitle.value,
                reminderTime = time.value
            )
            addHealthcareReminderUseCase(newReminder)
            
            // Auto-add to history
            historyRepository.addHistoryEntry(newReminder)
            
            _lastSavedReminderTitle.value = newReminder.title
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedReminderTitle.value = null
        _showSuccessDialog.value = false
    }
} 