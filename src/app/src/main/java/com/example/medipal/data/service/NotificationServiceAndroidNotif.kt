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
import androidx.core.content.ContextCompat
import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.service.NotificationService
import androidx.core.net.toUri
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class NotificationServiceAndroidNotif(
    private val context: Context,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager
) : NotificationService {

    override fun scheduleMedicationNotification(medication: Medication) {
        scheduleNotification(
            id = medication.id,
            title = medication.name,
            content = "Time to take ${medication.dosage}",
            scheduleTime = medication.scheduleTime,
            type = "MEDICATION"
        )
    }

    override fun scheduleAppointmentNotification(appointment: Appointment) {
        scheduleNotification(
            id = appointment.id,
            title = appointment.title,
            content = "Appointment with ${appointment.doctorName}",
            scheduleTime = appointment.dateTime,
            type = "APPOINTMENT"
        )
    }

    override fun scheduleReminderNotification(reminder: Reminder) {
        scheduleNotification(
            id = reminder.id,
            title = reminder.title,
            content = reminder.description.ifEmpty { "Health reminder" },
            scheduleTime = reminder.dateTime,
            type = "REMINDER"
        )
    }

    private fun scheduleNotification(
        id: String,
        title: String,
        content: String,
        scheduleTime: Long,
        type: String
    ) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("notification_id", id)
            putExtra("notification_title", title)
            putExtra("notification_content", content)
            putExtra("notification_type", type)
            // Add current profile ID to the notification intent
            try {
                val profileRepositoryManager = org.koin.core.context.GlobalContext.get().get<com.example.medipal.util.ProfileRepositoryManager>()
                putExtra("profile_id", profileRepositoryManager.getCurrentProfileId())
            } catch (e: Exception) {
                // If we can't get profile ID, don't add it
            }
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
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
                scheduleTime,
                pendingIntent
            )
        } catch (se: SecurityException) {
            Log.e("NotificationService", "Cannot schedule exact alarms: ${se.message}")
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                scheduleTime,
                pendingIntent
            )
        }
    }

    fun showSystemNotification(id: String, title: String, content: String) {
        try {
            // Check if we have notification permission
            if (ActivityCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("NotificationService", "POST_NOTIFICATIONS permission not granted")
                return
            }

            val notification = NotificationCompat.Builder(context, "medipal_channel")
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build()

            notificationManager.notify(id.hashCode(), notification)
            Log.d("NotificationService", "System notification shown: $title")
        } catch (e: Exception) {
            Log.e("NotificationService", "Failed to show system notification: ${e.message}")
        }
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
    
    fun cancelAllNotifications() {
        // Cancel all active notifications in the notification manager
        notificationManager.cancelAll()
    }
}