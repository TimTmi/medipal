package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.RemoveReminderUseCase
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class RemindersViewModel(
    private val getRemindersUseCase: GetRemindersUseCase,
    private val removeReminderUseCase: RemoveReminderUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    // Lắng nghe thay đổi profile và cập nhật dữ liệu động
    val reminders = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getRemindersUseCase(profileId)
    }

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
    
    fun clearData() {
        _selectedReminder.value = null
        _isDetailDialogVisible.value = false
        // Data will be automatically refreshed when profile changes
    }
}
