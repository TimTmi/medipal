package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
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
    
    override suspend fun addMedicationHistory(medication: Medication) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        
        val entry = HistoryEntry(
            id = UUID.randomUUID().toString(),
            day = now.format(formatter),
            time = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(medication.scheduleTime),
            type = "Medication",
            information = "${medication.name} (${medication.dosage})",
            timestamp = now
        )
        
        val currentList = historyFlow.value.toMutableList()
        currentList.add(0, entry)
        historyFlow.value = currentList
    }
    
    override suspend fun addAppointmentHistory(appointment: Appointment) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        
        val entry = HistoryEntry(
            id = UUID.randomUUID().toString(),
            day = now.format(formatter),
            time = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(appointment.scheduleTime),
            type = "Appointment",
            information = "${appointment.title} with ${appointment.doctor}",
            timestamp = now
        )
        
        val currentList = historyFlow.value.toMutableList()
        currentList.add(0, entry)
        historyFlow.value = currentList
    }
    
    override suspend fun addReminderHistory(reminder: Reminder) {
        val now = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
        
        val entry = HistoryEntry(
            id = UUID.randomUUID().toString(),
            day = now.format(formatter),
            time = java.text.SimpleDateFormat("HH:mm", Locale.getDefault()).format(reminder.scheduleTime),
            type = "Reminder",
            information = reminder.title,
            timestamp = now
        )
        
        val currentList = historyFlow.value.toMutableList()
        currentList.add(0, entry)
        historyFlow.value = currentList
    }
    
    override suspend fun clearHistory() {
        historyFlow.value = emptyList()
    }
} 