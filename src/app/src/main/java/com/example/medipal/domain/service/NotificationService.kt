package com.example.medipal.domain.service

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder

interface NotificationService {
    fun scheduleMedicationNotification(medication: Medication)
    fun scheduleAppointmentNotification(appointment: Appointment)
    fun scheduleReminderNotification(reminder: Reminder)
    fun cancelNotification(id: String)
}