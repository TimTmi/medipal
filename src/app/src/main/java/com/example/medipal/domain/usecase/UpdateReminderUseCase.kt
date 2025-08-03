package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository

class UpdateReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder) {
        repository.updateReminder(reminder)
    }
}
