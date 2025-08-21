package com.example.medipal.domain.usecase

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class GetAppointmentsUseCase(private val repository: Repository<Appointment>) {
    operator fun invoke(): Flow<List<Appointment>> {
        return repository.getAll()
    }
}