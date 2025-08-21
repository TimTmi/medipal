package com.example.medipal.di

import com.example.medipal.data.repository.HybridMedicationRepositoryImpl
import com.example.medipal.data.repository.RoomAppointmentRepositoryImpl
import org.koin.dsl.module
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
import com.example.medipal.data.repository.RoomReminderRepositoryImpl
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.Repository

val repositoryModule = module {
    single<Repository<Medication>> { RoomMedicationRepositoryImpl(get()) }
    single<Repository<Appointment>> { RoomAppointmentRepositoryImpl(get()) }
    single<Repository<Reminder>> { RoomReminderRepositoryImpl(get()) }
}