package com.example.medipal.data.repository

import com.example.medipal.domain.model.ScheduledEvent
import com.example.medipal.domain.repository.MedicationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class RoomMedicationRepositoryImpl : MedicationRepository {

    private val eventsFlow = MutableStateFlow<List<ScheduledEvent>>(emptyList())

    override fun getScheduledEvents(): Flow<List<ScheduledEvent>> {
        return eventsFlow
    }

    override suspend fun addMedication(medication: ScheduledEvent.Medication) {
        val currentList = eventsFlow.value.toMutableList()
        currentList.add(0, medication)
        eventsFlow.value = currentList
    }

    override suspend fun addHealthcareReminder(reminder: ScheduledEvent.Reminder) {
        val currentList = eventsFlow.value.toMutableList()
        currentList.add(0, reminder)
        eventsFlow.value = currentList
    }

    override suspend fun addAppointment(appointment: ScheduledEvent.Appointment) {
        val currentList = eventsFlow.value.toMutableList()
        currentList.add(0, appointment)
        eventsFlow.value = currentList
    }
}