package com.example.medipal.di

import androidx.room.Room
import com.example.medipal.data.local.database.MediPalDatabase
import org.koin.dsl.module

val databaseModule = module {

    // Provide the database
    single {
        Room.databaseBuilder(
            context = get(),
            klass = MediPalDatabase::class.java,
            name = "medipal.db"
        ).fallbackToDestructiveMigration(true).build()
    }

    // Provide DAOs
    single { get<MediPalDatabase>().medicationDao() }
    single { get<MediPalDatabase>().appointmentDao() }
    single { get<MediPalDatabase>().reminderDao() }
    single { get<MediPalDatabase>().profileDao() }
    single { get<MediPalDatabase>().medicationDoseDao() }
    single { get<MediPalDatabase>().appointmentStatusDao() }
    single { get<MediPalDatabase>().reminderStatusDao() }
}
