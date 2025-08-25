package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import kotlinx.coroutines.flow.Flow

class GetMedicationDoseUseCase(private val repository: MedicationDoseRepository) {
    operator fun invoke(profileId: String): Flow<List<MedicationDose>> {
        return repository.getByProfileId(profileId)
    }
    
    suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose? {
        return repository.getByMedicationAndTime(medicationId, scheduledTime)
    }
}
