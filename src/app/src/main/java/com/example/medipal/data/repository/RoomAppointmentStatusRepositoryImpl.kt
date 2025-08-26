package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.AppointmentStatusDao
import com.example.medipal.data.local.entity.AppointmentStatusEntity
import com.example.medipal.data.local.entity.toDomain
import com.example.medipal.data.local.entity.toEntity
import com.example.medipal.domain.model.AppointmentStatus
import com.example.medipal.domain.repository.AppointmentStatusRepository
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAppointmentStatusRepositoryImpl(
    private val dao: AppointmentStatusDao,
    private val profileRepositoryManager: ProfileRepositoryManager
) : RoomRepositoryImpl<AppointmentStatus, AppointmentStatusEntity>(
    getAllFlow = { dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    getAllOnceFunc = { dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    getByIdFunc = { dao.getById(it)?.takeIf { it.profileId == profileRepositoryManager.getCurrentProfileId() } },
    insert = { dao.insert(it) },
    updateFunc = { dao.update(it) },
    deleteById = { dao.deleteById(it) },
    toDomain = { it.toDomain() },
    toEntity = { it.toEntity() },
    getId = { it.id }
), AppointmentStatusRepository {

    override fun getAll(): Flow<List<AppointmentStatus>> {
        return dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId())
            .map { list -> list.map { it.toDomain() } }
    }
    
    override suspend fun getAllOnce(): List<AppointmentStatus> {
        return dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId())
            .map { it.toDomain() }
    }
    
    override suspend fun getById(id: String): AppointmentStatus? {
        return dao.getById(id)?.takeIf { it.profileId == profileRepositoryManager.getCurrentProfileId() }?.toDomain()
    }
    
    override suspend fun add(item: AppointmentStatus) {
        dao.insert(item.toEntity())
    }
    
    override suspend fun remove(id: String) {
        dao.deleteById(id)
    }
    
    override suspend fun update(item: AppointmentStatus) {
        dao.update(item.toEntity())
    }
    
    override fun getByAppointmentId(appointmentId: String): Flow<List<AppointmentStatus>> =
        dao.getByAppointmentIdAndProfileId(appointmentId, profileRepositoryManager.getCurrentProfileId())
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getByAppointmentAndTime(appointmentId: String, scheduledTime: Long): AppointmentStatus? =
        dao.getByAppointmentAndTimeAndProfileId(
            appointmentId,
            scheduledTime,
            profileRepositoryManager.getCurrentProfileId()
        )?.toDomain()
}
