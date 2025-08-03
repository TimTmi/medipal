package com.example.medipal.data.repository

import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.HistoryEntry
import com.example.medipal.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HistoryRepositoryImpl : HistoryRepository {
    
    private val historyFlow = MutableStateFlow<List<HistoryEntry>>(emptyList())
    
    override fun getHistoryEntries(): Flow<List<HistoryEntry>> {
        return historyFlow
    }
    
    override suspend fun addHistoryEntry(event: ScheduledEvent) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy") // Changed to 2-digit year
        
        val entry = when (event) {
            is ScheduledEvent.Medication -> {
                HistoryEntry(
                    id = UUID.randomUUID().toString(),
                    day = now.format(formatter),
                    time = event.time,
                    type = "Medication",
                    information = "${event.name} (${event.dosage})",
                    timestamp = now
                )
            }
            is ScheduledEvent.Appointment -> {
                HistoryEntry(
                    id = UUID.randomUUID().toString(),
                    day = now.format(formatter),
                    time = event.time,
                    type = "Appointment",
                    information = "${event.title} at ${event.location}",
                    timestamp = now
                )
            }
            is ScheduledEvent.Reminder -> {
                HistoryEntry(
                    id = UUID.randomUUID().toString(),
                    day = now.format(formatter),
                    time = event.time,
                    type = "Reminder",
                    information = event.title,
                    timestamp = now
                )
            }
        }
        
        val currentList = historyFlow.value.toMutableList()
        currentList.add(0, entry) // Add to top
        historyFlow.value = currentList
    }
    
    override suspend fun clearHistory() {
        historyFlow.value = emptyList()
    }
} 