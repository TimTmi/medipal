package com.example.medipal.domain.repository

import com.example.medipal.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {
    fun getReminders(): Flow<List<Reminder>>
    suspend fun addReminder(reminder: Reminder)
    suspend fun removeReminder(id: String)
    suspend fun updateReminder(reminder: Reminder)
}