package com.example.medipal.domain.service

import com.example.medipal.domain.model.Appointment

interface CalendarService {
    fun addEvent(appointment: Appointment)
    fun removeEvent(id: String)
}