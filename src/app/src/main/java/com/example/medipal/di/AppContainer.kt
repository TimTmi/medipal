package com.example.medipal.di

import android.content.Context
import androidx.room.Room
import com.example.medipal.data.local.database.MediPalDatabase
import com.example.medipal.data.repository.FakeAppointmentRepositoryImpl
import com.example.medipal.data.repository.FakeMedicationRepositoryImpl
import com.example.medipal.data.repository.FakeReminderRepositoryImpl
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
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

class DefaultAppContainer(context: Context) : AppContainer {

    private val database: MediPalDatabase = Room.databaseBuilder(
        context.applicationContext,
        MediPalDatabase::class.java,
        "medipal.db"
    ).build()
    // Repository
    override val medicationRepository: MedicationRepository by lazy {
        RoomMedicationRepositoryImpl(database.medicationDao())
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
