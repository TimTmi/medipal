package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetMedicationsUseCase(private val repository: Repository<Medication>) {
    operator fun invoke(): Flow<List<Medication>> {
        return repository.getAll()
    }
}