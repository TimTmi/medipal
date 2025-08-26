package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.MedicationDoseDao
import com.example.medipal.data.local.entity.MedicationDoseEntity
import com.example.medipal.data.local.entity.toDomain
import com.example.medipal.data.local.entity.toEntity
import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomMedicationDoseRepositoryImpl(
    private val dao: MedicationDoseDao,
    private val profileRepositoryManager: ProfileRepositoryManager
) : RoomRepositoryImpl<MedicationDose, MedicationDoseEntity>(
    getAllFlow = { dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    getAllOnceFunc = { dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    getByIdFunc = { dao.getById(it)?.takeIf { it.profileId == profileRepositoryManager.getCurrentProfileId() } },
    insert = { dao.insert(it) },
    updateFunc = { dao.update(it) },
    deleteById = { dao.deleteById(it) },
    toDomain = { it.toDomain() },
    toEntity = { it.toEntity() },
    getId = { it.id }
), MedicationDoseRepository {

    override fun getByMedicationId(medicationId: String) =
        dao.getByMedicationIdAndProfileId(medicationId, profileRepositoryManager.getCurrentProfileId())
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long) =
        dao.getByMedicationAndTimeAndProfileId(
            medicationId,
            scheduledTime,
            profileRepositoryManager.getCurrentProfileId()
        )?.toDomain()
}
