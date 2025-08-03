package com.example.medipal.domain.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    // Lấy tất cả medications
    fun getMedications(): Flow<List<Medication>>

    // Dùng cho use case thêm thuốc
    suspend fun addMedication(medication: Medication)
    
    // Cập nhật medication
    suspend fun updateMedication(medication: Medication)
    
    // Xóa medication
    suspend fun removeMedication(id: String)
}