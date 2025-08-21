package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.Repository

class AddAppointmentUseCase(private val repository: Repository<Appointment>) {
    suspend operator fun invoke(appointment: Appointment) {
        repository.add(appointment)
    }
}
