package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow

class GetAppointmentsUseCase(private val repository: AppointmentRepository) {
    operator fun invoke(): Flow<List<Appointment>> {
        return repository.getAppointments()
    }
}