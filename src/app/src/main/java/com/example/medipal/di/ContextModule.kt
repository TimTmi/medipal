package com.example.medipal.di

import android.content.Context
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val contextModule = module {
    single<Context> { androidContext() }
}