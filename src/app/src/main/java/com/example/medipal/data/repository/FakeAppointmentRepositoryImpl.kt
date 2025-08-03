package com.example.medipal.data.repository

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.AppointmentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeAppointmentRepositoryImpl : AppointmentRepository {

    private val appointmentsFlow = MutableStateFlow<List<Appointment>>(
        listOf(
            Appointment(
                id = "apt1",
                title = "Health Check-up",
                scheduleTime = System.currentTimeMillis() + 2_000_000,
                doctor = "Dr. Smith",
                notes = "Bring lab results"
            )
        )
    )

    override fun getAppointments(): Flow<List<Appointment>> {
        return appointmentsFlow
    }

    override suspend fun addAppointment(appointment: Appointment) {
        appointmentsFlow.value = listOf(appointment) + appointmentsFlow.value
    }

    override suspend fun removeAppointment(id: String) {
        appointmentsFlow.value = appointmentsFlow.value.filterNot { it.id == id }
    }

    override suspend fun updateAppointment(appointment: Appointment) {
        appointmentsFlow.value = appointmentsFlow.value.map {
            if (it.id == appointment.id) appointment else it
        }
    }
}
