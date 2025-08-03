package com.example.medipal.domain.usecase

import com.example.medipal.domain.repository.MedicationRepository

class RemoveMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(id: String) {
        repository.removeMedication(id)
    }
}
