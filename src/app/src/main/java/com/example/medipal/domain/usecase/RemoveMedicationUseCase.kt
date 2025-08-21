package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.Repository

class RemoveMedicationUseCase(private val repository: Repository<Medication>) {
    suspend operator fun invoke(id: String) {
        repository.remove(id)
    }
}
