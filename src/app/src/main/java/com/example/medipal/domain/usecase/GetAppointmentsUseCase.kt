package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetAppointmentsUseCase(private val repository: AppointmentRepository) {
    operator fun invoke(profileId: String): Flow<List<Appointment>> {
        return repository.getAll().map { appointments ->
            appointments.filter { it.profileId == profileId }
        }
    }
}