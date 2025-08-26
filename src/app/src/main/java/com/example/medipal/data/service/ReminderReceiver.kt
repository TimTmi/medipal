package com.example.medipal.data.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.medipal.domain.model.NotificationItem
import com.example.medipal.domain.model.NotificationStatus
import com.example.medipal.domain.model.NotificationType
import com.example.medipal.domain.service.InAppNotificationManager
import com.example.medipal.domain.service.NotificationService
import com.example.medipal.domain.service.AccountService
import com.example.medipal.util.ProfileRepositoryManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ReminderReceiver : BroadcastReceiver(), KoinComponent {
    private val notificationService: NotificationService by inject()
    private val accountService: AccountService by inject()
    private val profileRepositoryManager: ProfileRepositoryManager by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        val notificationId = intent?.getStringExtra("notification_id") ?: return
        val title = intent.getStringExtra("notification_title") ?: ""
        val content = intent.getStringExtra("notification_content") ?: ""
        val type = intent.getStringExtra("notification_type") ?: "REMINDER"
        val profileId = intent.getStringExtra("profile_id") ?: ""

        // Check if user is still authenticated and if this notification belongs to current profile
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val currentAccount = accountService.getCurrentAccount()
                val currentProfileId = profileRepositoryManager.getCurrentProfileId()

                // Only show notification if:
                // 1. User is still authenticated
                // 2. The notification belongs to the current profile
                if (currentAccount != null && (profileId.isEmpty() || profileId == currentProfileId)) {
                    // Show system notification
                    (notificationService as NotificationServiceAndroidNotif).showSystemNotification(notificationId, title, content)

        // Show in-app notification
        val notificationItem = NotificationItem(
            id = when (type) {
                "MEDICATION" -> "med_$notificationId"
                "APPOINTMENT" -> "apt_$notificationId"
                else -> "rem_$notificationId"
            },
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