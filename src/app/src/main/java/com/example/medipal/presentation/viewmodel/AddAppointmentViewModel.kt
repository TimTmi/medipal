package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.usecase.AddAppointmentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

class AddAppointmentViewModel(
    private val addAppointmentUseCase: AddAppointmentUseCase,
    private val historyRepository: HistoryRepository
) : ViewModel() {

    // Các trạng thái cho form
    val doctorName = MutableStateFlow("")
    val location = MutableStateFlow("")
    val date = MutableStateFlow("")
    val time = MutableStateFlow("")
    val reasonForVisit = MutableStateFlow("")
    
    // Trạng thái cho dialog thành công
    private val _showSuccessDialog = MutableStateFlow(false)
    val showSuccessDialog = _showSuccessDialog.asStateFlow()
    
    private val _lastSavedAppointmentTitle = MutableStateFlow<String?>(null)
    val lastSavedAppointmentTitle = _lastSavedAppointmentTitle.asStateFlow()

    fun saveAppointment() {
        viewModelScope.launch {
            val newAppointment = ScheduledEvent.Appointment(
                id = UUID.randomUUID().toString(),
                title = reasonForVisit.value.ifEmpty { "Appointment" },
                appointmentTime = time.value,
                doctor = doctorName.value,
                location = location.value,
                date = date.value
            )
            addAppointmentUseCase(newAppointment)
            
            // Auto-add to history
            historyRepository.addHistoryEntry(newAppointment)
            
            _lastSavedAppointmentTitle.value = newAppointment.title
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedAppointmentTitle.value = null
        _showSuccessDialog.value = false
    }
} 