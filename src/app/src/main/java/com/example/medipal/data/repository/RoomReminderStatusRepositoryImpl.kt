package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderStatusDao
import com.example.medipal.data.local.entity.ReminderStatusEntity
import com.example.medipal.data.local.entity.toDomain
import com.example.medipal.data.local.entity.toEntity
import com.example.medipal.domain.model.ReminderStatus
import com.example.medipal.domain.repository.ReminderStatusRepository
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomReminderStatusRepositoryImpl(
    private val dao: ReminderStatusDao,
    private val profileRepositoryManager: ProfileRepositoryManager
) : RoomRepositoryImpl<ReminderStatus, ReminderStatusEntity>(
    getAllFlow = { dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    getAllOnceFunc = { dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    getByIdFunc = { dao.getById(it)?.takeIf { it.profileId == profileRepositoryManager.getCurrentProfileId() } },
    insert = { dao.insert(it) },
    updateFunc = { dao.update(it) },
    deleteById = { dao.deleteById(it) },
    toDomain = { it.toDomain() },
    toEntity = { it.toEntity() },
    getId = { it.id }
), ReminderStatusRepository {

    override fun getAll(): Flow<List<ReminderStatus>> {
        return dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId())
            .map { list -> list.map { it.toDomain() } }
    }

    override suspend fun getAllOnce(): List<ReminderStatus> {
        return dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId())
            .map { it.toDomain() }
    }

    override suspend fun getById(id: String): ReminderStatus? {
        return dao.getById(id)?.takeIf { it.profileId == profileRepositoryManager.getCurrentProfileId() }?.toDomain()
    }

    override suspend fun add(item: ReminderStatus) {
        dao.insert(item.toEntity())
    }

    override suspend fun remove(id: String) {
        dao.deleteById(id)
    }

    override suspend fun update(item: ReminderStatus) {
        dao.update(item.toEntity())
    }

    override fun getByReminderId(reminderId: String): Flow<List<ReminderStatus>> =
        dao.getByReminderIdAndProfileId(reminderId, profileRepositoryManager.getCurrentProfileId())
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getByReminderAndTime(reminderId: String, scheduledTime: Long): ReminderStatus? =
        dao.getByReminderAndTimeAndProfileId(
            reminderId,
            scheduledTime,
            profileRepositoryManager.getCurrentProfileId()
        )?.toDomain()
}
