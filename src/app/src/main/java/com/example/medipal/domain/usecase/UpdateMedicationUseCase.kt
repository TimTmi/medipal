package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository

class UpdateMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(medication: Medication) {
        repository.updateMedication(medication)
    }
}
