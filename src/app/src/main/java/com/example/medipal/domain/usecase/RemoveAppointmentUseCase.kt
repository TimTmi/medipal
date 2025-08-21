package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.Repository

class RemoveAppointmentUseCase(private val repository: Repository<Appointment>) {
    suspend operator fun invoke(id: String) {
        repository.remove(id)
    }
}