package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.AppointmentStatus
import com.example.medipal.domain.repository.AppointmentStatusRepository
import kotlinx.coroutines.flow.Flow

class GetAppointmentStatusUseCase(
    private val repository: AppointmentStatusRepository
) {
    fun getAll(): Flow<List<AppointmentStatus>> {
        return repository.getAll()
    }
    
    suspend fun getByAppointmentAndTime(appointmentId: String, scheduledTime: Long): AppointmentStatus? {
        return repository.getByAppointmentAndTime(appointmentId, scheduledTime)
    }
    
    fun getByAppointmentId(appointmentId: String): Flow<List<AppointmentStatus>> {
        return repository.getByAppointmentId(appointmentId)
    }
}
