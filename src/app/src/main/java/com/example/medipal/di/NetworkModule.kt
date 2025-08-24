package com.example.medipal.di

import com.example.medipal.util.NetworkChecker
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val networkModule = module {
    single { NetworkChecker(get()) }
    single<FirebaseFirestore> {
        FirebaseFirestore.getInstance()
    }
}