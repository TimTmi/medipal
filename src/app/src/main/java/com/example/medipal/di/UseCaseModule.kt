package com.example.medipal.di

import com.example.medipal.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {

    // Medication use cases
    factory { GetMedicationsUseCase(get()) }
    factory { GetMedicationByIdUseCase(get()) }
    factory { AddMedicationUseCase(get()) }
    factory { RemoveMedicationUseCase(get()) }
    factory { UpdateMedicationUseCase(get()) }

    // Appointment use cases
    factory { GetAppointmentsUseCase(get()) }
    factory { AddAppointmentUseCase(get()) }
    factory { RemoveAppointmentUseCase(get()) }
    factory { UpdateAppointmentUseCase(get()) }

    // Reminder use cases
    factory { GetRemindersUseCase(get()) }
    factory { AddReminderUseCase(get()) }
    factory { RemoveReminderUseCase(get()) }
    factory { UpdateReminderUseCase(get()) }

    // Combined / shared
    factory { GetScheduledEventsUseCase(get(), get(), get()) }
}
