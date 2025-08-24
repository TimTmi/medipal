package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetRemindersUseCase(private val repository: ReminderRepository) {
    operator fun invoke(profileId: String): Flow<List<Reminder>> {
        return repository.getAll().map { reminders ->
            reminders.filter { it.profileId == profileId }
        }
    }
}