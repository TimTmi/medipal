package com.example.medipal.domain.repository

import com.example.medipal.domain.model.MedicationDose
import kotlinx.coroutines.flow.Flow

interface MedicationDoseRepository : Repository<MedicationDose> {
    fun getByMedicationId(medicationId: String): Flow<List<MedicationDose>>
    suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose?
    fun getByProfileId(profileId: String): Flow<List<MedicationDose>>
}
