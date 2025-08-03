package com.example.medipal.di

import com.example.medipal.presentation.viewmodel.*
import org.koin.core.module.dsl.*
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { HomeViewModel(get(), get(), get()) }

    viewModel { AddMedicationViewModel(get()) }

    viewModel { CalendarViewModel() }

    // Add more ViewModels here as needed
}