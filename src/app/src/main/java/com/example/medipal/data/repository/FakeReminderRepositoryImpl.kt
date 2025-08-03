package com.example.medipal.data.repository

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeReminderRepositoryImpl : ReminderRepository {

    private val remindersFlow = MutableStateFlow<List<Reminder>>(
        listOf(
            Reminder(
                id = "rem1",
                title = "Go for a walk",
                scheduleTime = System.currentTimeMillis() + 4_000_000,
                notes = "30 minutes around the block"
            )
        )
    )

    override fun getReminders(): Flow<List<Reminder>> {
        return remindersFlow
    }

    override suspend fun addReminder(reminder: Reminder) {
        remindersFlow.value = listOf(reminder) + remindersFlow.value
    }

    override suspend fun removeReminder(id: String) {
        remindersFlow.value = remindersFlow.value.filterNot { it.id == id }
    }

    override suspend fun updateReminder(reminder: Reminder) {
        remindersFlow.value = remindersFlow.value.map {
            if (it.id == reminder.id) reminder else it
        }
    }
}
