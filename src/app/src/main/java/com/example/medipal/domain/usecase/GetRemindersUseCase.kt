package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetRemindersUseCase(private val repository: Repository<Reminder>) {
    operator fun invoke(): Flow<List<Reminder>> {
        return repository.getAll()
    }
}