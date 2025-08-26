package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.RemoveAppointmentUseCase
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class AppointmentsViewModel(
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val removeAppointmentUseCase: RemoveAppointmentUseCase,
    private val profileRepositoryManager: ProfileRepositoryManager
) : ViewModel() {

    val appointments = profileRepositoryManager.currentProfileId.flatMapLatest { profileId ->
        getAppointmentsUseCase(profileId)
    }

    private val _selectedAppointment = MutableStateFlow<Appointment?>(null)
    val selectedAppointment = _selectedAppointment.asStateFlow()

    private val _isDetailDialogVisible = MutableStateFlow(false)
    val isDetailDialogVisible = _isDetailDialogVisible.asStateFlow()

    fun showAppointmentDetail(appointment: Appointment) {
        _selectedAppointment.value = appointment
        _isDetailDialogVisible.value = true
    }

    fun hideAppointmentDetail() {
        _isDetailDialogVisible.value = false
        _selectedAppointment.value = null
    }

    fun deleteAppointment(appointmentId: String) {
        viewModelScope.launch {
            removeAppointmentUseCase(appointmentId)
            hideAppointmentDetail()
        }
    }

    fun clearData() {
        _selectedAppointment.value = null
        _isDetailDialogVisible.value = false
        // Data will be automatically refreshed when profile changes
    }
}
