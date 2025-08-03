package com.example.medipal.domain.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface HistoryRepository {
    fun getHistoryEntries(): Flow<List<HistoryEntry>>
    suspend fun addMedicationHistory(medication: Medication)
    suspend fun addAppointmentHistory(appointment: Appointment)
    suspend fun addReminderHistory(reminder: Reminder)
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