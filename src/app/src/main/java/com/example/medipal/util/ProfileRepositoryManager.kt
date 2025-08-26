package com.example.medipal.util

import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ProfileRepositoryManager : KoinComponent {
    private val _currentProfileId = MutableStateFlow("default-profile")
    val currentProfileId: StateFlow<String> = _currentProfileId.asStateFlow()

    fun setCurrentProfile(profileId: String) {
        _currentProfileId.value = profileId.ifBlank { "default-profile" }
    }

    fun getCurrentProfileId(): String = _currentProfileId.value.ifBlank { "default-profile" }
}

