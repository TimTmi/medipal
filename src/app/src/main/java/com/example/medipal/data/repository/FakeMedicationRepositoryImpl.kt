package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeMedicationRepositoryImpl : MedicationRepository {

    private val medicationsFlow = MutableStateFlow<List<Medication>>(
        listOf(
            Medication(
                id = "med1",
                name = "Paracetamol",
                dosage = "500mg, take 1 tablet(s)",
                scheduleTime = System.currentTimeMillis(), // or any fixed test value
                notes = "Before breakfast"
            )
        )
    )

    override fun getMedications(): Flow<List<Medication>> {
        return medicationsFlow
    }

    override suspend fun addMedication(medication: Medication) {
        val currentList = medicationsFlow.value.toMutableList()
        currentList.add(0, medication)
        medicationsFlow.value = currentList
    }

    override suspend fun removeMedication(id: String) {
        medicationsFlow.value = medicationsFlow.value.filterNot { it.id == id }
    }

    override suspend fun updateMedication(medication: Medication) {
        medicationsFlow.value = medicationsFlow.value.map {
            if (it.id == medication.id) medication else it
        }
    }
}
