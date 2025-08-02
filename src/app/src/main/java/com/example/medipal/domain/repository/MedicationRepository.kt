package com.example.medipal.domain.repository

import com.example.medipal.domain.model.ScheduledEvent
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.Async.Schedule

interface MedicationRepository {
    // Sẽ lấy tất cả các sự kiện để hiển thị trên màn hình chính
    fun getScheduledEvents(): Flow<List<ScheduledEvent>>

    // Dùng cho use case thêm thuốc
    suspend fun addMedication(medication: ScheduledEvent.Medication)
    suspend fun addHealthcareReminder(reminder: ScheduledEvent.Reminder)
}