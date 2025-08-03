package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RoomMedicationRepositoryImpl : MedicationRepository {

    private val medicationsFlow = MutableStateFlow<List<Medication>>(emptyList())

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