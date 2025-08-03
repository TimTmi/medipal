package com.example.medipal.di

import com.example.medipal.data.repository.FakeMedicationRepositoryImpl
import com.example.medipal.data.repository.HistoryRepositoryImpl
import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.usecase.AddHealthcareReminderUseCase
import com.example.medipal.domain.usecase.AddAppointmentUseCase
import com.example.medipal.domain.usecase.GetScheduledEventsUseCase

/**
 * Dependency injection container at the application level.
 */
interface AppContainer {
    val medicationRepository: MedicationRepository
    val historyRepository: HistoryRepository
    val getScheduledEventsUseCase: GetScheduledEventsUseCase
    val addMedicationUseCase: AddMedicationUseCase
    val addHealthcareReminderUseCase: AddHealthcareReminderUseCase
    val addAppointmentUseCase: AddAppointmentUseCase
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
    
    override val historyRepository: HistoryRepository by lazy {
        HistoryRepositoryImpl()
    }
    
    // Use Cases
    override val getScheduledEventsUseCase: GetScheduledEventsUseCase by lazy {
        GetScheduledEventsUseCase(medicationRepository)
    }
    
    override val addMedicationUseCase: AddMedicationUseCase by lazy {
        AddMedicationUseCase(medicationRepository)
    }
    
    override val addHealthcareReminderUseCase: AddHealthcareReminderUseCase by lazy {
        AddHealthcareReminderUseCase(medicationRepository)
    }
    
    override val addAppointmentUseCase: AddAppointmentUseCase by lazy {
        AddAppointmentUseCase(medicationRepository)
    }
}
