package com.example.medipal.di

import com.example.medipal.presentation.viewmodel.ManageCaregiverViewModel
import com.example.medipal.presentation.viewmodel.*
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(get(), get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { AddMedicationViewModel(get(), get(), get()) }
    viewModel { AddHealthcareReminderViewModel(get(), get(), get()) }
    viewModel { AddAppointmentViewModel(get(), get(), get()) }
    viewModel { MedicationListViewModel(get(), get(), get(), get()) }
    viewModel { MedicationDetailViewModel(get(), get(), get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get(), get()) }
    viewModel { CalendarViewModel() }
    viewModel { ProfileViewModel(get()) }
    viewModel { NotificationViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AppointmentsViewModel(get(), get(), get()) }
    viewModel { RemindersViewModel(get(), get(), get()) }
    viewModel { AppointmentReminderViewModel(get(), get(), get(), get(), get()) }
    viewModel { ManageCaregiverViewModel(get(), get()) }
}