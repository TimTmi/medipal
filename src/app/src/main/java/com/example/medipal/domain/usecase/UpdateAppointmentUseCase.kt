package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.AppointmentRepository

class UpdateAppointmentUseCase(private val repository: AppointmentRepository) {
    suspend operator fun invoke(appointment: Appointment) {
        repository.updateAppointment(appointment)
    }
}