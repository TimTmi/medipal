package com.example.medipal.data.repository

import com.example.medipal.data.local.dao.ReminderDao
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomReminderRepositoryImpl(
    private val dao: ReminderDao
) : ReminderRepository {

    override fun getReminders(): Flow<List<Reminder>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addReminder(reminder: Reminder) {
        dao.insert(reminder.toEntity())
    }

    override suspend fun removeReminder(id: String) {
        dao.deleteById(id)
    }

    override suspend fun updateReminder(reminder: Reminder) {
        dao.update(reminder.toEntity())
    }
}