package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository

class RemoveReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(id: String) {
        repository.removeReminder(id)
    }
}
