package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.Repository


class AddMedicationUseCase(private val repository: Repository<Medication>) {
    suspend operator fun invoke(medication: Medication) {
        repository.add(medication)
    }
}