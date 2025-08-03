package com.example.medipal.domain.repository

import com.example.medipal.domain.model.Medication
import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun getMedications(): Flow<List<Medication>>
    suspend fun addMedication(medication: Medication)
    suspend fun removeMedication(id: String)
    suspend fun updateMedication(medication: Medication)
}