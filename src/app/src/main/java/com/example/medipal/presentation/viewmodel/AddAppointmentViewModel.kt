package com.example.medipal.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.medipal.domain.model.Appointment
//import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.usecase.AddAppointmentUseCase
import com.example.medipal.domain.service.NotificationService
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import java.text.SimpleDateFormat
import java.util.Locale

class AddAppointmentViewModel(
    private val addAppointmentUseCase: AddAppointmentUseCase,
    private val notificationService: NotificationService,
    private val profileRepositoryManager: ProfileRepositoryManager
//    private val historyRepository: HistoryRepository
//    private val profileId: String
) : ViewModel() {

    private val profileId = profileRepositoryManager.getCurrentProfileId()

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
            // Convert user-selected time to timestamp with current date
            val timeString = time.value.ifEmpty { "09:00 AM" }
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
            
            val newAppointment = Appointment(
                id = UUID.randomUUID().toString(),
                title = reasonForVisit.value.ifEmpty { "Appointment" },
                dateTime = scheduledTimestamp,
                doctorName = doctorName.value,
                location = location.value,
                description = "Date: ${date.value}"
            )
            addAppointmentUseCase(newAppointment, profileId)
            
            // Schedule notification
            notificationService.scheduleAppointmentNotification(newAppointment)
            
            // Auto-add to history
//            historyRepository.addAppointmentHistory(newAppointment)
            
            _lastSavedAppointmentTitle.value = newAppointment.title
            
            // Reset form trước khi hiển thị dialog để tránh hiệu ứng nhấp nháy
            resetForm()
            _showSuccessDialog.value = true // Hiển thị dialog sau khi lưu
        }
    }

    fun dismissSuccessDialog() {
        _lastSavedAppointmentTitle.value = null
        _showSuccessDialog.value = false
    }
    
    private fun resetForm() {
        doctorName.value = ""
        location.value = ""
        date.value = ""
        time.value = ""
        reasonForVisit.value = ""
    }
    
    // Thêm function public để reset form khi cần
    fun clearForm() {
        resetForm()
    }
} 