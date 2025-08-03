package com.example.medipal

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import com.example.medipal.di.repositoryModule

class MediPalApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MediPalApp)
            modules(repositoryModule) // <- your Koin modules go here
        }
    }
}
