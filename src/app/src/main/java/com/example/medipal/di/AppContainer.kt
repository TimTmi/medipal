package com.example.medipal.di

import android.content.Context
import androidx.room.Room
import com.example.medipal.data.local.database.MediPalDatabase
import com.example.medipal.data.repository.FakeAppointmentRepositoryImpl
import com.example.medipal.data.repository.FakeMedicationRepositoryImpl
import com.example.medipal.data.repository.FakeReminderRepositoryImpl
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
//import com.example.medipal.data.repository.HistoryRepositoryImpl
import com.example.medipal.domain.repository.AppointmentRepository
//import com.example.medipal.domain.repository.HistoryRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.domain.usecase.AddMedicationUseCase
import com.example.medipal.domain.usecase.AddReminderUseCase
import com.example.medipal.domain.usecase.AddAppointmentUseCase
import com.example.medipal.domain.usecase.GetMedicationsUseCase
import com.example.medipal.domain.usecase.GetAppointmentsUseCase
import com.example.medipal.domain.usecase.GetRemindersUseCase
import com.example.medipal.domain.usecase.UpdateMedicationUseCase
import com.example.medipal.domain.usecase.RemoveMedicationUseCase

/**
 * Dependency injection container at the application level.
 */
interface AppContainer {
    val medicationRepository: MedicationRepository
    val appointmentRepository: AppointmentRepository
    val reminderRepository: ReminderRepository
//    val historyRepository: HistoryRepository
    val getMedicationsUseCase: GetMedicationsUseCase
    val getAppointmentsUseCase: GetAppointmentsUseCase
    val getRemindersUseCase: GetRemindersUseCase
    val addMedicationUseCase: AddMedicationUseCase
    val addReminderUseCase: AddReminderUseCase
    val addAppointmentUseCase: AddAppointmentUseCase
    val updateMedicationUseCase: UpdateMedicationUseCase
    val removeMedicationUseCase: RemoveMedicationUseCase
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
    
//    override val historyRepository: HistoryRepository by lazy {
//        HistoryRepositoryImpl()
//    }

    // Use Cases
    override val getMedicationsUseCase: GetMedicationsUseCase by lazy {
        GetMedicationsUseCase(medicationRepository)
    }

    override val getAppointmentsUseCase: GetAppointmentsUseCase by lazy {
        GetAppointmentsUseCase(appointmentRepository)
    }

    override val getRemindersUseCase: GetRemindersUseCase by lazy {
        GetRemindersUseCase(reminderRepository)
    }
    
    override val addMedicationUseCase: AddMedicationUseCase by lazy {
        AddMedicationUseCase(medicationRepository)
    }

    override val addReminderUseCase: AddReminderUseCase by lazy {
        AddReminderUseCase(reminderRepository)
    }

    override val addAppointmentUseCase: AddAppointmentUseCase by lazy {
        AddAppointmentUseCase(appointmentRepository)
    }

    override val updateMedicationUseCase: UpdateMedicationUseCase by lazy {
        UpdateMedicationUseCase(medicationRepository)
    }

    override val removeMedicationUseCase: RemoveMedicationUseCase by lazy {
        RemoveMedicationUseCase(medicationRepository)
    }
}
