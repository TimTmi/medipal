package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository

class AddReminderUseCase(private val repository: ReminderRepository) {
    suspend operator fun invoke(reminder: Reminder, profileId: String) {
        val reminderWithProfile = reminder.copy(profileId = profileId)
        repository.add(reminderWithProfile)
    }
}