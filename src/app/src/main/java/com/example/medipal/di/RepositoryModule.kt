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
    factory { (profileId: String) -> 
        RoomMedicationRepositoryImpl(get(), profileId) 
    }
    factory { (profileId: String) -> 
        RoomAppointmentRepositoryImpl(get(), profileId) 
    }
    factory { (profileId: String) -> 
        RoomReminderRepositoryImpl(get(), profileId) 
    }

    // Profile-scoped Firestore repositories
    factory { (profileId: String) -> 
        FirestoreMedicationRepositoryImpl(get(), profileId) 
    }
    factory { (profileId: String) -> 
        FirestoreAppointmentRepositoryImpl(get(), profileId) 
    }
    factory { (profileId: String) -> 
        FirestoreReminderRepositoryImpl(get(), profileId) 
    }

    // Profile-scoped Hybrid repositories
    factory { (profileId: String) -> 
        HybridMedicationRepositoryImpl(
            get<RoomMedicationRepositoryImpl> { parametersOf(profileId) },
            get<FirestoreMedicationRepositoryImpl> { parametersOf(profileId) },
            { get<NetworkChecker>().isOnline() }
        )
    }
    factory { (profileId: String) -> 
        HybridAppointmentRepositoryImpl(
            get<RoomAppointmentRepositoryImpl> { parametersOf(profileId) },
            get<FirestoreAppointmentRepositoryImpl> { parametersOf(profileId) },
            { get<NetworkChecker>().isOnline() }
        )
    }
    factory { (profileId: String) -> 
        HybridReminderRepositoryImpl(
            get<RoomReminderRepositoryImpl> { parametersOf(profileId) },
            get<FirestoreReminderRepositoryImpl> { parametersOf(profileId) },
            { get<NetworkChecker>().isOnline() }
        )
    }

    // Legacy single repositories for backward compatibility (using default profile)
    single<MedicationRepository> { 
        HybridMedicationRepositoryImpl(
            get<RoomMedicationRepositoryImpl> { parametersOf("default-profile") },
            get<FirestoreMedicationRepositoryImpl> { parametersOf("default-profile") },
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
}