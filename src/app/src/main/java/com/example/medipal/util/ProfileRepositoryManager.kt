package com.example.medipal.util

import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class ProfileRepositoryManager : KoinComponent {
    
    private var currentProfileId: String = ""
    
    fun setCurrentProfile(profileId: String) {
        currentProfileId = profileId
    }
    
    fun getCurrentProfileId(): String = currentProfileId
    
    fun getMedicationRepository(): MedicationRepository {
        val repository: MedicationRepository by inject { parametersOf(currentProfileId) }
        return repository
    }
    
    fun getAppointmentRepository(): AppointmentRepository {
        val repository: AppointmentRepository by inject { parametersOf(currentProfileId) }
        return repository
    }
    
    fun getReminderRepository(): ReminderRepository {
        val repository: ReminderRepository by inject { parametersOf(currentProfileId) }
        return repository
    }
}
