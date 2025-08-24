package com.example.medipal.data.service

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.service.NotificationService
import androidx.core.net.toUri

class NotificationServiceAndroidNotif(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager
) : NotificationService {

    override fun scheduleNotification(reminder: Reminder) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("reminder_id", reminder.id)
            putExtra("reminder_title", reminder.title)
            putExtra("reminder_notes", reminder.notes)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val settingsIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = "package:${context.packageName}".toUri()
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(settingsIntent)
                return
            }
        }

        try {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.scheduleTime,
                pendingIntent
            )
        } catch (se: SecurityException) {
            Log.e("NotificationService", "Cannot schedule exact alarms: ${se.message}")

            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.scheduleTime,
                pendingIntent
            )
        }
    }

    fun showNotification(reminder: Reminder) {
        val notification = NotificationCompat.Builder(context, "medipal_channel")
            .setContentTitle(reminder.title)
            .setContentText(reminder.notes ?: "You have a reminder")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(reminder.id.hashCode(), notification)
    }

    override fun cancelNotification(id: String) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
        notificationManager.cancel(id.hashCode())
    }
}