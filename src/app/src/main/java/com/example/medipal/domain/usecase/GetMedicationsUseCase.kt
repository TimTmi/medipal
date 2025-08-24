package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetMedicationsUseCase(private val repository: MedicationRepository) {
    operator fun invoke(profileId: String): Flow<List<Medication>> {
        return repository.getAll().map { medications ->
            medications.filter { it.profileId == profileId }
        }
    }
}