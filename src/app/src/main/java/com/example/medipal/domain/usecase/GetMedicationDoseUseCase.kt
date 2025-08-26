package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMedicationDoseUseCase(private val repository: MedicationDoseRepository) {
    operator fun invoke(profileId: String): Flow<List<MedicationDose>> {
        return repository.getAll().map { medication_doses ->
            medication_doses.filter { it.profileId == profileId }
        }
    }
    
    suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose? {
        return repository.getByMedicationAndTime(medicationId, scheduledTime)
    }
}
