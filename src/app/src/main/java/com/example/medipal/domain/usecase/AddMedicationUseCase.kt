package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository

class AddMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(medication: Medication, profileId: String) {
        val medicationWithProfile = medication.copy(profileId = profileId)
        repository.add(medicationWithProfile)
    }
}