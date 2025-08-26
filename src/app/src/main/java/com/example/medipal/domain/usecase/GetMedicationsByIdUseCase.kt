package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository

class GetMedicationByIdUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(id: String, profileId: String): Medication? {
        val medication = repository.getById(id)
        return medication?.takeIf { it.profileId == profileId }
    }
}