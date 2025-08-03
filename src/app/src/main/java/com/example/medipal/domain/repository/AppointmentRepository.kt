package com.example.medipal.domain.repository

import com.example.medipal.domain.model.Appointment
import kotlinx.coroutines.flow.Flow

interface AppointmentRepository {
    fun getAppointments(): Flow<List<Appointment>>
    suspend fun addAppointment(appointment: Appointment)
    suspend fun removeAppointment(id: String)
    suspend fun updateAppointment(appointment: Appointment)
}