package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow

class GetMedicationsUseCase(private val repository: MedicationRepository) {
    operator fun invoke(): Flow<List<Medication>> {
        return repository.getMedications()
    }
}
class GetMedicationByIdUseCase(private val repository: MedicationRepository) {
    operator fun invoke(id: String): Flow<Medication?> = repository.getMedicationsById(id)
}