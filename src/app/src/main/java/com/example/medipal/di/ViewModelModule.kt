package com.example.medipal.di

import com.example.medipal.presentation.viewmodel.*
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get(), get()) }
    viewModel { AddMedicationViewModel(get(), get()) }
    viewModel { AddHealthcareReminderViewModel(get(), get()) }
    viewModel { AddAppointmentViewModel(get(), get()) }
    viewModel { MedicationListViewModel(get(), get(), get()) }
    viewModel { HistoryViewModel(get(), get(), get()) }
    viewModel { CalendarViewModel() }
    viewModel { ProfileViewModel() }
    viewModel { NotificationViewModel(get(), get(), get()) }
}