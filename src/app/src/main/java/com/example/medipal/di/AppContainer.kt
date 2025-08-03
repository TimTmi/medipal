package com.example.medipal.di

import com.example.medipal.data.repository.FakeAppointmentRepositoryImpl
import com.example.medipal.data.repository.FakeMedicationRepositoryImpl
import com.example.medipal.data.repository.FakeReminderRepositoryImpl
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.usecase.GetScheduledEventsUseCase

/**
 * Dependency injection container at the application level.
 */
interface AppContainer {
    val medicationRepository: MedicationRepository
    val appointmentRepository: AppointmentRepository
    val reminderRepository: ReminderRepository
    val getScheduledEventsUseCase: GetScheduledEventsUseCase
    val addMedicationUseCase: AddMedicationUseCase
}

/**
 * Implementation for the Dependency Injection container at the application level.
 *
 * Variables are initialized lazily and the same instance is shared across the whole app.
 */
class DefaultAppContainer : AppContainer {
    
    // Repository
    override val medicationRepository: MedicationRepository by lazy {
        FakeMedicationRepositoryImpl()
    }

    override val appointmentRepository: AppointmentRepository by lazy {
        FakeAppointmentRepositoryImpl()
    }

    override val reminderRepository: ReminderRepository by lazy {
        FakeReminderRepositoryImpl()
    }
    
    // Use Cases
    override val getScheduledEventsUseCase: GetScheduledEventsUseCase by lazy {
        GetScheduledEventsUseCase(medicationRepository, appointmentRepository, reminderRepository)
    }
    
    override val addMedicationUseCase: AddMedicationUseCase by lazy {
        AddMedicationUseCase(medicationRepository)
    }
}
