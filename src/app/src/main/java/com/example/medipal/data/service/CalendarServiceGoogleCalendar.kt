package com.example.medipal.data.service

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.service.CalendarService
import com.google.api.client.util.DateTime
import com.google.api.services.calendar.Calendar
import com.google.api.services.calendar.model.Event
import com.google.api.services.calendar.model.EventDateTime

class CalendarServiceGoogleCalendar(
    private val calendarApi: Calendar
) : CalendarService {

    override fun addEvent(appointment: Appointment) {
        // Default duration: 1 hour
        val startMillis = appointment.scheduleTime
        val endMillis = startMillis + 60 * 60 * 1000 // 1 hour later

        val event = Event().apply {
            summary = appointment.title
            description = appointment.notes
            start = EventDateTime().setDateTime(DateTime(startMillis))
            end = EventDateTime().setDateTime(DateTime(endMillis))
        }

        try {
            calendarApi.events()
                .insert("primary", event)
                .execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun removeEvent(id: String) {
        try {
            calendarApi.events()
                .delete("primary", id)
                .execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}