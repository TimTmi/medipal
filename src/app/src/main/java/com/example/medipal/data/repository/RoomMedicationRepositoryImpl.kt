package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDao
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomMedicationRepositoryImpl(
    private val dao: MedicationDao
) : MedicationRepository {

    override fun getMedications(): Flow<List<Medication>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addMedication(medication: Medication) {
        dao.insert(medication.toEntity())
    }

    override suspend fun removeMedication(id: String) {
        dao.deleteById(id)
    }

    override suspend fun updateMedication(medication: Medication) {
        dao.update(medication.toEntity())
    }
}