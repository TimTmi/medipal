package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.data.local.entity.ReminderEntity
import com.example.medipal.domain.model.Reminder
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import com.example.medipal.domain.repository.ReminderRepository

class RoomReminderRepositoryImpl(
    dao: ReminderDao
) : RoomRepositoryImpl<Reminder, ReminderEntity>(
    { dao.getAll() },
    { dao.getAllOnce() },
    { dao.getById(it) },
    { dao.insert(it) },
    { dao.update(it) },
    { dao.deleteById(it) },
    { it.toDomain() },
    { it.toEntity() },
    { it.id }
), ReminderRepository