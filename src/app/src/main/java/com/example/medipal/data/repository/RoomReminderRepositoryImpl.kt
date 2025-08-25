package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.data.local.entity.ReminderEntity
import com.example.medipal.domain.model.Reminder
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.util.ProfileRepositoryManager

class RoomReminderRepositoryImpl(
    private val dao: ReminderDao,
    private val profileRepositoryManager: ProfileRepositoryManager
) : RoomRepositoryImpl<Reminder, ReminderEntity>(
    { dao.getAllByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    { dao.getAllOnceByProfileId(profileRepositoryManager.getCurrentProfileId()) },
    { dao.getByIdAndProfileId(it, profileRepositoryManager.getCurrentProfileId()) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteByIdAndProfileId(it, profileRepositoryManager.getCurrentProfileId()) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), ReminderRepository