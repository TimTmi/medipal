package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.ReminderStatus
import com.example.medipal.domain.repository.ReminderStatusRepository

class UpdateReminderStatusUseCase(
    private val repository: ReminderStatusRepository
) {
    suspend operator fun invoke(reminderStatus: ReminderStatus) {
        repository.update(reminderStatus)
    }
}
