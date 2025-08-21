package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.Repository

class AddReminderUseCase(private val repository: Repository<Reminder>) {
    suspend operator fun invoke(reminder: Reminder) {
        repository.add(reminder)
    }
}