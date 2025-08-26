package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.ReminderStatus
import com.example.medipal.domain.repository.ReminderStatusRepository
import kotlinx.coroutines.flow.Flow

class GetReminderStatusUseCase(
    private val repository: ReminderStatusRepository
) {
    fun getAll(): Flow<List<ReminderStatus>> {
        return repository.getAll()
    }
    
    suspend fun getByReminderAndTime(reminderId: String, scheduledTime: Long): ReminderStatus? {
        return repository.getByReminderAndTime(reminderId, scheduledTime)
    }
    
    fun getByReminderId(reminderId: String): Flow<List<ReminderStatus>> {
        return repository.getByReminderId(reminderId)
    }
}
