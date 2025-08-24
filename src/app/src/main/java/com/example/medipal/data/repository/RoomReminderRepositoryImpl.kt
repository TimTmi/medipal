package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.data.local.entity.ReminderEntity
import com.example.medipal.domain.model.Reminder
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.ReminderRepository

class RoomReminderRepositoryImpl(
    private val dao: ReminderDao,
    private val profileId: String
) : RoomRepositoryImpl<Reminder, ReminderEntity>(
    { dao.getAllByProfileId(profileId) },
    { dao.getAllOnceByProfileId(profileId) },
    { dao.getByIdAndProfileId(it, profileId) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteByIdAndProfileId(it, profileId) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), ReminderRepository