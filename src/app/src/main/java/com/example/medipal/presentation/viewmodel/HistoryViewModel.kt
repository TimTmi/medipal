package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

data class HistoryItem(
    val id: String,
    val day: String,
    val time: String,
    val type: String,
    val information: String,
    val timestamp: Long
)

class HistoryViewModel(
    private val getMedicationsUseCase: GetMedicationsUseCase,
    private val getAppointmentsUseCase: GetAppointmentsUseCase,
    private val getRemindersUseCase: GetRemindersUseCase
) : ViewModel() {

    val historyItems: Flow<List<HistoryItem>> = combine(
        getMedicationsUseCase(),
        getAppointmentsUseCase(),
        getRemindersUseCase()
    ) { medications, appointments, reminders ->
        val allItems = mutableListOf<HistoryItem>()
        
        // Convert medications to history items
        allItems.addAll(medications.map { medication ->
            medication.toHistoryItem()
        })
        
        // Convert appointments to history items
        allItems.addAll(appointments.map { appointment ->
            appointment.toHistoryItem()
        })
        
        // Convert reminders to history items
        allItems.addAll(reminders.map { reminder ->
            reminder.toHistoryItem()
        })
        
        // Sort by timestamp (most recent first)
        allItems.sortedByDescending { it.timestamp }
    }
}

// Extension functions to convert entities to HistoryItem
private fun Medication.toHistoryItem(): HistoryItem {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    // Convert timestamp to LocalDateTime using actual event time
    val eventDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this.scheduleTime),
        ZoneId.systemDefault()
    )
    
    return HistoryItem(
        id = this.id,
        day = dateFormatter.format(eventDateTime),
        time = timeFormatter.format(eventDateTime),
        type = "Medication",
        information = "${this.name} (${this.dosage})",
        timestamp = this.scheduleTime
    )
}

private fun Appointment.toHistoryItem(): HistoryItem {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    // Convert timestamp to LocalDateTime using actual event time
    val eventDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this.scheduleTime),
        ZoneId.systemDefault()
    )
    
    return HistoryItem(
        id = this.id,
        day = dateFormatter.format(eventDateTime),
        time = timeFormatter.format(eventDateTime),
        type = "Appointment",
        information = "${this.title} with ${this.doctor}",
        timestamp = this.scheduleTime
    )
}

private fun Reminder.toHistoryItem(): HistoryItem {
    val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")
    
    // Convert timestamp to LocalDateTime using actual event time
    val eventDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochMilli(this.scheduleTime),
        ZoneId.systemDefault()
    )
    
    return HistoryItem(
        id = this.id,
        day = dateFormatter.format(eventDateTime),
        time = timeFormatter.format(eventDateTime),
        type = "Reminder",
        information = this.title,
        timestamp = this.scheduleTime
    )
}
