package com.example.medipal.di

import org.koin.dsl.module
import com.example.medipal.data.repository.RoomMedicationRepositoryImpl
import com.example.medipal.domain.repository.MedicationRepository

val repositoryModule = module {
    single<MedicationRepository> { RoomMedicationRepositoryImpl() }
}