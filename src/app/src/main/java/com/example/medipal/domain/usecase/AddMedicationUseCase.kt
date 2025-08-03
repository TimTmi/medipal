package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository

//class GetScheduledEventsUseCase(private val repository: MedicationRepository) {
//    operator fun invoke() = repository.getScheduledEvents()
//}

class AddMedicationUseCase(private val repository: MedicationRepository) {
    suspend operator fun invoke(medication: Medication) {
        repository.addMedication(medication)
    }
}