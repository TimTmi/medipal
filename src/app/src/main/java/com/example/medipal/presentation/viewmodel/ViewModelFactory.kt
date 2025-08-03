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
                HomeViewModel(
                    container.getMedicationsUseCase,
                    container.getAppointmentsUseCase,
                    container.getRemindersUseCase
                ) as T
            }
            AddMedicationViewModel::class.java -> {
                AddMedicationViewModel(container.addMedicationUseCase) as T
            AddMedicineViewModel::class.java -> {
                AddMedicineViewModel(container.addMedicationUseCase, container.historyRepository) as T
            }
            AddHealthcareReminderViewModel::class.java -> {
                AddHealthcareReminderViewModel(container.addReminderUseCase, container.historyRepository) as T
            }
            AddAppointmentViewModel::class.java -> {
                AddAppointmentViewModel(container.addAppointmentUseCase, container.historyRepository) as T
            }
            MedicationListViewModel::class.java -> {
                MedicationListViewModel(
                    container.getMedicationsUseCase,
                    container.updateMedicationUseCase,
                    container.removeMedicationUseCase
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
