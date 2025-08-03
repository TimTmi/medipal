package com.example.medipal.data.repository

import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.model.Appointment
import com.example.medipal.data.local.dao.AppointmentDao
import com.example.medipal.data.mapper.toDomain
import com.example.medipal.data.mapper.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomAppointmentRepositoryImpl(
    private val dao: AppointmentDao
) : AppointmentRepository {

    override fun getAppointments(): Flow<List<Appointment>> {
        return dao.getAll().map { list -> list.map { it.toDomain() } }
    }

    override suspend fun addAppointment(appointment: Appointment) {
        dao.insert(appointment.toEntity())
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        dao.update(appointment.toEntity())
    }

    override suspend fun removeAppointment(id: String) {
        dao.deleteById(id)
    }
}
