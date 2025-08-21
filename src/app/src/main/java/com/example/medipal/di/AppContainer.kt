package com.example.medipal.di

import android.content.Context
import androidx.room.Room
import com.example.medipal.data.local.database.MediPalDatabase
import com.example.medipal.data.repository.RoomAppointmentRepositoryImpl
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
import com.example.medipal.data.repository.RoomReminderRepositoryImpl
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.Repository
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
    val medicationRepository: Repository<Medication>
    val appointmentRepository: Repository<Appointment>
    val reminderRepository: Repository<Reminder>
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
    ).fallbackToDestructiveMigration(false).build()
    // Repository
    override val medicationRepository: Repository<Medication> by lazy {
        RoomMedicationRepositoryImpl(database.medicationDao())
    }

    override val appointmentRepository: Repository<Appointment> by lazy {
        RoomAppointmentRepositoryImpl(database.appointmentDao())
    }

    override val reminderRepository: Repository<Reminder> by lazy {
        RoomReminderRepositoryImpl(database.reminderDao())
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
