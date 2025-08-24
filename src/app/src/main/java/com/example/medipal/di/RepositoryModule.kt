package com.example.medipal.di

import com.example.medipal.data.repository.*
import org.koin.dsl.module
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.*
import com.example.medipal.util.NetworkChecker

val repositoryModule = module {

//    single<MedicationRepository> { RoomMedicationRepositoryImpl(get(), get()) }
//    single<AppointmentRepository> { RoomAppointmentRepositoryImpl(get(), get()) }
//    single<ReminderRepository> { RoomReminderRepositoryImpl(get(), get()) }

//    single<MedicationRepository> { FirestoreMedicationRepositoryImpl(get()) }
//    single<ReminderRepository> { FirestoreReminderRepositoryImpl(get()) }
//    single<AppointmentRepository> { FirestoreAppointmentRepositoryImpl(get()) }

    single { RoomMedicationRepositoryImpl(get()) }
    single { RoomAppointmentRepositoryImpl(get()) }
    single { RoomReminderRepositoryImpl(get()) }

    single { FirestoreMedicationRepositoryImpl(get()) }
    single { FirestoreAppointmentRepositoryImpl(get()) }
    single { FirestoreReminderRepositoryImpl(get()) }

    single<MedicationRepository> { HybridMedicationRepositoryImpl(
        get<RoomMedicationRepositoryImpl>(),
        get<FirestoreMedicationRepositoryImpl>(),
        { get<NetworkChecker>().isOnline() })
    }
    single<AppointmentRepository> {
        HybridAppointmentRepositoryImpl(
        get<RoomAppointmentRepositoryImpl>(),
        get<FirestoreAppointmentRepositoryImpl>(),
        { get<NetworkChecker>().isOnline() })
    }
    single<ReminderRepository> {
        HybridReminderRepositoryImpl(
        get<RoomReminderRepositoryImpl>(),
        get<FirestoreReminderRepositoryImpl>(),
        { get<NetworkChecker>().isOnline() })
    }
}