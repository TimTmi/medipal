package com.example.medipal.domain.model

sealed class ScheduledItem(open val scheduleTime: Long) {
    data class MedicationItem(val data: Medication) : ScheduledItem(data.scheduleTime)
    data class AppointmentItem(val data: Appointment) : ScheduledItem(data.dateTime)
    data class ReminderItem(val data: Reminder) : ScheduledItem(data.dateTime)
}