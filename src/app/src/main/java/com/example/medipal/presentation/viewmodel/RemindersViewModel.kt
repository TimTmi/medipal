package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.RemoveReminderUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RemindersViewModel(
    getRemindersUseCase: GetRemindersUseCase,
    private val removeReminderUseCase: RemoveReminderUseCase
) : ViewModel() {

    val reminders = getRemindersUseCase()

    private val _selectedReminder = MutableStateFlow<Reminder?>(null)
    val selectedReminder = _selectedReminder.asStateFlow()

    private val _isDetailDialogVisible = MutableStateFlow(false)
    val isDetailDialogVisible = _isDetailDialogVisible.asStateFlow()

    fun showReminderDetail(reminder: Reminder) {
        _selectedReminder.value = reminder
        _isDetailDialogVisible.value = true
    }

    fun hideReminderDetail() {
        _isDetailDialogVisible.value = false
        _selectedReminder.value = null
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            removeReminderUseCase(reminderId)
            hideReminderDetail()
        }
    }
}
