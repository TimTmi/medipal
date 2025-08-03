package com.example.medipal.domain.model

sealed class ScheduledEvent(val time: String) {
    data class Medication(
        val id: String,
        val name: String,
        val dosage: String,
        private val medicationTime: String
    ) : ScheduledEvent(medicationTime) // Truyền thời gian cho lớp cha

    data class Appointment(
        val id: String,
        val title: String,
        private val appointmentTime: String,
        val doctor: String,
        val location: String,
        val date: String
    ) : ScheduledEvent(appointmentTime)

    data class Reminder(
        val id: String,
        val title: String,
        private val reminderTime: String
    ) : ScheduledEvent(reminderTime)
}