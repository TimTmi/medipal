package com.example.medipal.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.medipal.di.AppContainer

/**
 * Factory for ViewModels
 */
//class ViewModelFactory(
//    owner: SavedStateRegistryOwner,
//    private val container: AppContainer
//) : AbstractSavedStateViewModelFactory(owner, null) {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(
//        key: String,
//        modelClass: Class<T>,
//        handle: SavedStateHandle // handle này đã được cung cấp sẵn cho chúng ta
//    ): T {
//        return when (modelClass) {
//            HomeViewModel::class.java -> {
//                HomeViewModel(
//                    container.getMedicationsUseCase,
//                    container.getAppointmentsUseCase,
//                    container.getRemindersUseCase
//                ) as T
//            }
//            AddMedicationViewModel::class.java -> {
//                AddMedicationViewModel(container.addMedicationUseCase) as T
//            }
//            AddHealthcareReminderViewModel::class.java -> {
//                AddHealthcareReminderViewModel(container.addReminderUseCase) as T
//            }
//            AddAppointmentViewModel::class.java -> {
//                AddAppointmentViewModel(container.addAppointmentUseCase) as T
//            }
//            MedicationListViewModel::class.java -> {
//                MedicationListViewModel(
//                    container.getMedicationsUseCase,
//                    container.updateMedicationUseCase,
//                    container.removeMedicationUseCase
//                ) as T
//            }
//            HistoryViewModel::class.java -> {
//                HistoryViewModel(
//                    container.getMedicationsUseCase,
//                    container.getAppointmentsUseCase,
//                    container.getRemindersUseCase
//                ) as T
//            }
//            MedicationDetailViewModel::class.java -> {
//
//                MedicationDetailViewModel(
//                    savedStateHandle = handle, // <-- Truyền vào đây
//                    getMedicationByIdUseCase = container.getMedicationByIdUseCase,
//                    updateMedicationUseCase = container.updateMedicationUseCase,
//                    deleteMedicationUseCase = container.removeMedicationUseCase
//                ) as T
//            }
//            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//        }
//    }
//}


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
            }
            AddHealthcareReminderViewModel::class.java -> {
                AddHealthcareReminderViewModel(container.addReminderUseCase) as T
            }
            AddAppointmentViewModel::class.java -> {
                AddAppointmentViewModel(container.addAppointmentUseCase) as T
            }
            MedicationListViewModel::class.java -> {
                MedicationListViewModel(
                    container.getMedicationsUseCase,
                    container.updateMedicationUseCase,
                    container.removeMedicationUseCase
                ) as T
            }
            MedicationDetailViewModel::class.java -> {
                val savedStateHandle = extras.createSavedStateHandle()
                MedicationDetailViewModel(
                    savedStateHandle = savedStateHandle,
                    getMedicationByIdUseCase = container.getMedicationByIdUseCase,
                    updateMedicationUseCase = container.updateMedicationUseCase,
                    deleteMedicationUseCase = container.removeMedicationUseCase
                ) as T
            }
            HistoryViewModel::class.java -> {
                HistoryViewModel(
                    container.getMedicationsUseCase,
                    container.getAppointmentsUseCase,
                    container.getRemindersUseCase
                ) as T
            }
            // ... thêm các ViewModel khác ...

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
