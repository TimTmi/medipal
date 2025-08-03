package com.example.medipal.domain.repository

import com.example.medipal.domain.model.ScheduledEvent
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    // Sẽ lấy tất cả các sự kiện để hiển thị trên màn hình chính
    fun getScheduledEvents(): Flow<List<ScheduledEvent>>

    // Dùng cho use case thêm thuốc
    suspend fun addMedication(medication: ScheduledEvent.Medication)
    
    // Dùng cho use case thêm healthcare reminder
    suspend fun addHealthcareReminder(reminder: ScheduledEvent.Reminder)
    
    // Dùng cho use case thêm cuộc hẹn
    suspend fun addAppointment(appointment: ScheduledEvent.Appointment)
}