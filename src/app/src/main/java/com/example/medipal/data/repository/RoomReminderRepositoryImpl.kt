package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.data.local.entity.ReminderEntity
import com.example.medipal.domain.model.Reminder
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity

class RoomReminderRepositoryImpl(
    dao: ReminderDao
) : RoomRepositoryImpl<Reminder, ReminderEntity>(
    getAllFlow = { dao.getAll() },
    insert = { dao.insert(it) },
    update = { dao.update(it) },
    deleteById = { dao.deleteById(it) },
    toDomain = { it.toDomain() },
    toEntity = { it.toEntity() }
)