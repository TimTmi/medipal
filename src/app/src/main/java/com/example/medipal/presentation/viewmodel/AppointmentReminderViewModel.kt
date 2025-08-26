package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.RemoveAppointmentUseCase
import com.example.medipal.domain.usecase.RemoveReminderUseCase
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class AppointmentReminderViewModel(
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val removeAppointmentUseCase: RemoveAppointmentUseCase,
    private val removeReminderUseCase: RemoveReminderUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    val appointments = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getAppointmentsUseCase(profileId)
    }
    
    val reminders = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getRemindersUseCase(profileId)
    }

    // Search functionality
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible = _isSearchVisible.asStateFlow()

    // Dialog states for appointments
    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment = _selectedAppointment.asStateFlow()

    private val _isAppointmentDetailVisible = MutableStateFlow(false)
    val isAppointmentDetailVisible = _isAppointmentDetailVisible.asStateFlow()

    // Dialog states for reminders
    private val _selectedReminder = MutableStateFlow<Reminder?>(null)
    val selectedReminder = _selectedReminder.asStateFlow()

    private val _isReminderDetailVisible = MutableStateFlow(false)
    val isReminderDetailVisible = _isReminderDetailVisible.asStateFlow()

    // Add sheet state
    private val _isAddSheetVisible = MutableStateFlow(false)
    val isAddSheetVisible = _isAddSheetVisible.asStateFlow()

    // Filtered data
    val filteredAppointments = combine(appointments, searchQuery) { appointments, query ->
        if (query.isBlank()) appointments
        else appointments.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.doctorName.contains(query, ignoreCase = true) 
        }
    }

    val filteredReminders = combine(reminders, searchQuery) { reminders, query ->
        if (query.isBlank()) reminders
        else reminders.filter { 
            it.title.contains(query, ignoreCase = true) || 
            it.description.contains(query, ignoreCase = true) 
        }
    }

    // Search functions
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun showSearch() {
        _isSearchVisible.value = true
    }

    fun hideSearch() {
        _isSearchVisible.value = false
        _searchQuery.value = ""
    }

    // Appointment dialog functions
    fun showAppointmentDetail(appointment: Appointment) {
        _selectedAppointment.value = appointment
        _isAppointmentDetailVisible.value = true
    }

    fun hideAppointmentDetail() {
        _isAppointmentDetailVisible.value = false
        _selectedAppointment.value = null
    }

    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            removeAppointmentUseCase(appointmentId)
            hideAppointmentDetail()
        }
    }

    // Reminder dialog functions
    fun showReminderDetail(reminder: Reminder) {
        _selectedReminder.value = reminder
        _isReminderDetailVisible.value = true
    }

    fun hideReminderDetail() {
        _isReminderDetailVisible.value = false
        _selectedReminder.value = null
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            removeReminderUseCase(reminderId)
            hideReminderDetail()
        }
    }

    // Add sheet functions
    fun showAddSheet() {
        _isAddSheetVisible.value = true
    }

    fun hideAddSheet() {
        _isAddSheetVisible.value = false
    }
    
    fun clearData() {
        _searchQuery.value = ""
        _isSearchVisible.value = false
        _selectedAppointment.value = null
        _isAppointmentDetailVisible.value = false
        _selectedReminder.value = null
        _isReminderDetailVisible.value = false
        _isAddSheetVisible.value = false
        // Data will be automatically refreshed when profile changes
    }
}
