package com.example.medipal.data.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.medipal.R
import com.example.medipal.domain.model.Reminder
import com.example.medipal.data.service.NotificationServiceAndroidNotif

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("reminder_title") ?: "Reminder"
        val notes = intent.getStringExtra("reminder_notes")

        val reminder = Reminder(
            id = intent.getStringExtra("reminder_id") ?: System.currentTimeMillis().toString(),
            title = title,
            scheduleTime = System.currentTimeMillis(),
            notes = notes
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationService = NotificationServiceAndroidNotif(
            context,
            alarmManager,
            notificationManager
        )

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            notificationService.showNotification(reminder)
        }
    }
}