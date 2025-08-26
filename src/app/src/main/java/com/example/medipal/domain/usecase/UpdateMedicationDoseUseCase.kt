package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository

class UpdateMedicationDoseUseCase(private val repository: MedicationDoseRepository) {
    suspend operator fun invoke(dose: MedicationDose) {
        repository.update(dose)
    }
}
