package com.example.medipal.di

import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import org.koin.dsl.module

fun profileScopedModule(profileId: String) = module {
    
    // Profile-scoped repositories
    single<MedicationRepository> { 
        get<MedicationRepository>()
    }
    
    single<AppointmentRepository> { 
        get<AppointmentRepository>()
    }
    
    single<ReminderRepository> { 
        get<ReminderRepository>()
    }
}
