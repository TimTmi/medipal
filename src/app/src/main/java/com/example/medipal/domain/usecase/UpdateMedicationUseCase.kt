package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.Repository

class UpdateMedicationUseCase(private val repository: Repository<Medication>) {
    suspend operator fun invoke(medication: Medication) {
        repository.update(medication)
    }
}
