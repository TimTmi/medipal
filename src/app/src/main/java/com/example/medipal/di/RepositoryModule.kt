package com.example.medipal.di

import com.example.medipal.data.repository.*
import org.koin.dsl.module
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.*
import com.example.medipal.util.NetworkChecker
import com.example.medipal.util.ProfileRepositoryManager
import com.example.medipal.util.ProfileInitializer
import org.koin.core.parameter.parametersOf

val repositoryModule = module {

    // Profile management
    single { ProfileRepositoryManager() }
    single { ProfileInitializer() }

    // Profile-scoped Room repositories
    single { RoomMedicationRepositoryImpl(get(), get()) }
    single { RoomAppointmentRepositoryImpl(get(), get()) }
    single { RoomReminderRepositoryImpl(get(), get()) }

    // Profile-scoped Firestore repositories
    single { FirestoreMedicationRepositoryImpl(get(), get()) }
    single { FirestoreAppointmentRepositoryImpl(get(), get()) }
    single { FirestoreReminderRepositoryImpl(get(), get()) }

//    // Profile-scoped Hybrid repositories
    single { HybridMedicationRepositoryImpl(
        get<RoomMedicationRepositoryImpl>(),
        get<FirestoreMedicationRepositoryImpl>(),
        { get<NetworkChecker>().isOnline() }
        )
    }
    single { HybridAppointmentRepositoryImpl(
        get<RoomAppointmentRepositoryImpl>(),
        get<FirestoreAppointmentRepositoryImpl>(),
            { get<NetworkChecker>().isOnline() }
        )
    }
    single { HybridReminderRepositoryImpl(
        get<RoomReminderRepositoryImpl>(),
        get<FirestoreReminderRepositoryImpl>(),
            { get<NetworkChecker>().isOnline() }
        )
    }
    single<AppointmentRepository> {
        HybridAppointmentRepositoryImpl(
            get<RoomAppointmentRepositoryImpl> { parametersOf("default-profile") },
            get<FirestoreAppointmentRepositoryImpl> { parametersOf("default-profile") },
            { get<NetworkChecker>().isOnline() }
        )
    }
    single<ReminderRepository> {
        HybridReminderRepositoryImpl(
            get<RoomReminderRepositoryImpl> { parametersOf("default-profile") },
            get<FirestoreReminderRepositoryImpl> { parametersOf("default-profile") },
            { get<NetworkChecker>().isOnline() }
        )
    }

    // Profile-scoped MedicationDose repositories
    factory { (profileId: String) ->
        RoomMedicationDoseRepositoryImpl(get(), profileId)
    }
    factory { (profileId: String) ->
        FirestoreMedicationDoseRepositoryImpl(get(), profileId)
    }
    factory { (profileId: String) ->
        HybridMedicationDoseRepositoryImpl(
            get<RoomMedicationDoseRepositoryImpl> { parametersOf(profileId) },
            get<FirestoreMedicationDoseRepositoryImpl> { parametersOf(profileId) },
            { get<NetworkChecker>().isOnline() }
        )
    }

    // MedicationDose repository (using default profile)
    single<MedicationDoseRepository> {
        HybridMedicationDoseRepositoryImpl(
            get<RoomMedicationDoseRepositoryImpl> { parametersOf("default-profile") },
            get<FirestoreMedicationDoseRepositoryImpl> { parametersOf("default-profile") },
            { get<NetworkChecker>().isOnline() }
        )
    }

    single<MedicationRepository> { get<HybridMedicationRepositoryImpl>() }
    single<AppointmentRepository> { get<HybridAppointmentRepositoryImpl>() }
    single<ReminderRepository> { get<HybridReminderRepositoryImpl>() }
}