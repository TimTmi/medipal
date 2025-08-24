package com.example.medipal.di

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import com.example.medipal.data.service.NotificationServiceAndroidNotif
import com.example.medipal.domain.service.NotificationService
import com.example.medipal.data.service.FirebaseAccountServiceImpl
import com.example.medipal.domain.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val serviceModule = module {
    // Firebase instances
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    
    // Account Service
    single<AccountService> {
        FirebaseAccountServiceImpl(
            auth = get(),
            firestore = get()
        )
    }
    
    // Notification Service
    single<NotificationService> {
        NotificationServiceAndroidNotif(
            context = get(),
            alarmManager = get<Context>().getSystemService(Context.ALARM_SERVICE) as AlarmManager,
            notificationManager = get<Context>().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        )
    }
}
