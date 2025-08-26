package com.example.medipal.domain.repository

import com.example.medipal.domain.model.ReminderStatus
import kotlinx.coroutines.flow.Flow

interface ReminderStatusRepository : Repository<ReminderStatus> {
    suspend fun getByReminderAndTime(reminderId: String, scheduledTime: Long): ReminderStatus?
    fun getByReminderId(reminderId: String): Flow<List<ReminderStatus>>
}
