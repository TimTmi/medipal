package com.example.medipal.domain.repository

import com.example.medipal.domain.model.ScheduledEvent
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface HistoryRepository {
    fun getHistoryEntries(): Flow<List<HistoryEntry>>
    suspend fun addHistoryEntry(event: ScheduledEvent)
    suspend fun clearHistory()
}

data class HistoryEntry(
    val id: String,
    val day: String,
    val time: String,
    val type: String,
    val information: String,
    val timestamp: LocalDateTime
) 