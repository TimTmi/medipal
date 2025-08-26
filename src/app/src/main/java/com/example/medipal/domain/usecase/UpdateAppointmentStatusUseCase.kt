package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.AppointmentStatus
import com.example.medipal.domain.repository.AppointmentStatusRepository

class UpdateAppointmentStatusUseCase(
    private val repository: AppointmentStatusRepository
) {
    suspend operator fun invoke(appointmentStatus: AppointmentStatus) {
        repository.update(appointmentStatus)
    }
}
