package com.example.medipal.domain.repository

import com.example.medipal.domain.model.AppointmentStatus
import kotlinx.coroutines.flow.Flow

interface AppointmentStatusRepository : Repository<AppointmentStatus> {
    suspend fun getByAppointmentAndTime(appointmentId: String, scheduledTime: Long): AppointmentStatus?
    fun getByAppointmentId(appointmentId: String): Flow<List<AppointmentStatus>>
}
