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
    single { RoomMedicationDoseRepositoryImpl(get(), get()) }
    single { RoomAppointmentStatusRepositoryImpl(get(), get()) }
    single { RoomReminderStatusRepositoryImpl(get(), get()) }

    // Profile-scoped Firestore repositories
    single { FirestoreMedicationRepositoryImpl(get(), get()) }
    single { FirestoreAppointmentRepositoryImpl(get(), get()) }
    single { FirestoreReminderRepositoryImpl(get(), get()) }
    single { FirestoreMedicationDoseRepositoryImpl(get(), get()) }
    single { FirestoreCaregiverAssignmentRepositoryImpl(get()) }
    single { FirestoreAppointmentStatusRepositoryImpl(get(), get()) }
    single { FirestoreReminderStatusRepositoryImpl(get(), get()) }

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
    single { HybridMedicationDoseRepositoryImpl(
            get<RoomMedicationDoseRepositoryImpl>(),
            get<FirestoreMedicationDoseRepositoryImpl>(),
            { get<NetworkChecker>().isOnline() }
    )
    }
    single { HybridAppointmentStatusRepositoryImpl(
            get<RoomAppointmentStatusRepositoryImpl>(),
            get<FirestoreAppointmentStatusRepositoryImpl>(),
            { get<NetworkChecker>().isOnline() }
    )
    }
    single { HybridReminderStatusRepositoryImpl(
            get<RoomReminderStatusRepositoryImpl>(),
            get<FirestoreReminderStatusRepositoryImpl>(),
            { get<NetworkChecker>().isOnline() }
    )
    }

    single<MedicationRepository> { get<HybridMedicationRepositoryImpl>() }
    single<AppointmentRepository> { get<HybridAppointmentRepositoryImpl>() }
    single<ReminderRepository> { get<HybridReminderRepositoryImpl>() }
    single<MedicationDoseRepository> { get<HybridMedicationDoseRepositoryImpl>()}
    single<CaregiverAssignmentRepository> { get<FirestoreCaregiverAssignmentRepositoryImpl>() }
    single<AppointmentStatusRepository> { get<HybridAppointmentStatusRepositoryImpl>()}
    single<ReminderStatusRepository> { get<HybridReminderStatusRepositoryImpl>()}
}