package com.example.medipal.di

import com.example.medipal.util.NetworkChecker
import org.koin.dsl.module

val networkModule = module {
    // Provide NetworkChecker with Android Context
    single { NetworkChecker(get()) }
}