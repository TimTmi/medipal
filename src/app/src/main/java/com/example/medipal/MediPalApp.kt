package com.example.medipal

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.medipal.data.repository.HybridRepositoryImpl
import com.example.medipal.di.databaseModule
import com.example.medipal.di.networkModule
import com.example.medipal.di.repositoryModule
import com.example.medipal.di.serviceModule
import com.example.medipal.di.useCaseModule
import com.example.medipal.di.viewModelModule
import com.example.medipal.domain.repository.AppointmentRepository
import com.example.medipal.domain.repository.MedicationRepository
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.util.SyncManager
import coroutineModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin

class MediPalApp : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        // Create notification channel
        createNotificationChannel()

        startKoin {
            androidContext(this@MediPalApp)
            modules(
                databaseModule,
                repositoryModule,
                useCaseModule,
                viewModelModule,
                networkModule,
                serviceModule,
                coroutineModule
                // Add others if needed
            )
        }

        val medicationRepo = get<MedicationRepository>() as? HybridRepositoryImpl<*>
        val appointmentRepo = get<AppointmentRepository>() as? HybridRepositoryImpl<*>
        val reminderRepo = get<ReminderRepository>() as? HybridRepositoryImpl<*>

        // Register all repos (listeners + network monitoring)
        SyncManager.register(
            *listOfNotNull(medicationRepo, appointmentRepo, reminderRepo).toTypedArray()
        )

        // Start network monitoring
        SyncManager.startMonitoring(this)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "medipal_channel",
                "MediPal Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Medication, appointment, and health reminders"
                enableVibration(true)
                enableLights(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
