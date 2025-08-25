package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDoseDao
import com.example.medipal.data.local.entity.toDomain
import com.example.medipal.data.local.entity.toEntity
import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomMedicationDoseRepositoryImpl(
    private val dao: MedicationDoseDao,
    private val profileId: String
) : MedicationDoseRepository {

    override fun getAll(): Flow<List<MedicationDose>> {
        return dao.getAllByProfileId(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getAllOnce(): List<MedicationDose> {
        return dao.getAllOnceByProfileId(profileId).map { it.toDomain() }
    }

    override suspend fun getById(id: String): MedicationDose? {
        return dao.getById(id)?.toDomain()
    }

    override suspend fun add(item: MedicationDose) {
        dao.insert(item.toEntity())
    }

    override suspend fun remove(id: String) {
        dao.deleteById(id)
    }

    override suspend fun update(item: MedicationDose) {
        dao.update(item.toEntity())
    }

    override fun getByMedicationId(medicationId: String): Flow<List<MedicationDose>> {
        return dao.getByMedicationId(medicationId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose? {
        return dao.getByMedicationAndTime(medicationId, scheduledTime)?.toDomain()
    }

    override fun getByProfileId(profileId: String): Flow<List<MedicationDose>> {
        return dao.getAllByProfileId(profileId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
