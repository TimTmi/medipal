package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.data.local.dao.SyncDao
import com.example.medipal.data.local.entity.ReminderEntity
import com.example.medipal.domain.model.Reminder
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.ReminderRepository

class RoomReminderRepositoryImpl(
    dao: ReminderDao,
    syncDao: SyncDao
) : RoomRepositoryImpl<Reminder, ReminderEntity>(
    { dao.getAll() },
    { dao.getAllOnce() },
    { dao.getById(it) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteById(it) },
    syncDao,
    "Reminder",
    { it.toDomain() },
    { it.toEntity() },
    { it.id },
    { it.updatedAt } // use a timestamp in Reminder
), ReminderRepository