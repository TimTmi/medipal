package com.example.medipal

import android.app.Application
import com.example.medipal.di.databaseModule
import com.example.medipal.di.networkModule
import com.example.medipal.di.repositoryModule
import com.example.medipal.di.useCaseModule
import com.example.medipal.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MediPalApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MediPalApp)
            modules(
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                networkModule
                // Add others if needed
            )
        }
    }
}
