package com.example.medipal.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.example.medipal.data.service.NotificationServiceAndroidNotif
import com.example.medipal.domain.service.NotificationService
import org.koin.dsl.module

val serviceModule = module {
    single<NotificationService> {
        NotificationServiceAndroidNotif(
            context = get(),
            alarmManager = get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            notificationManager = get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )
    }
}
