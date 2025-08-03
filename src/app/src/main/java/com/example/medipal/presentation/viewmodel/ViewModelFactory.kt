package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.medipal.di.AppContainer

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when (modelClass) {
            HomeViewModel::class.java -> {
                HomeViewModel(container.getScheduledEventsUseCase) as T
            }
            AddMedicationViewModel::class.java -> {
                AddMedicationViewModel(container.addMedicationUseCase) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
