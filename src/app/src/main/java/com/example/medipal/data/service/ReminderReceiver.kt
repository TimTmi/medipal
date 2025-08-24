package com.example.medipal.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medipal.domain.model.NotificationItem
import com.example.medipal.domain.model.NotificationStatus
import com.example.medipal.domain.model.NotificationType
import com.example.medipal.domain.service.InAppNotificationManager
import com.example.medipal.domain.service.NotificationService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderReceiver : BroadcastReceiver(), KoinComponent {
    private val notificationService: NotificationService by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.getStringExtra("notification_id") ?: return
        val title = intent.getStringExtra("notification_title") ?: ""
        val content = intent.getStringExtra("notification_content") ?: ""
        val type = intent.getStringExtra("notification_type") ?: "REMINDER"

        // Show system notification
        (notificationService as NotificationServiceAndroidNotif).showSystemNotification(notificationId, title, content)

        // Show in-app notification
        val notificationItem = NotificationItem(
            id = notificationId,
            title = title,
            subtitle = content,
            time = System.currentTimeMillis().toString(),
            scheduleTime = System.currentTimeMillis(),
            status = NotificationStatus.UPCOMING,
            type = when (type) {
                "MEDICATION" -> NotificationType.MEDICATION
                "APPOINTMENT" -> NotificationType.APPOINTMENT
                else -> NotificationType.REMINDER
            },
            instructions = content,
            doctorName = "",
            originalItem = null
        )

        CoroutineScope(Dispatchers.Main).launch {
            InAppNotificationManager.showNotification(notificationItem)
        }
    }
}