package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.Repository

class RemoveReminderUseCase(private val repository: Repository<Reminder>) {
    suspend operator fun invoke(id: String) {
        repository.remove(id)
    }
}
