package com.example.medipal.domain.service

import com.example.medipal.domain.model.NotificationItem
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object InAppNotificationManager {
    private val _notificationFlow = MutableSharedFlow<NotificationItem>()
    val notificationFlow: SharedFlow<NotificationItem> = _notificationFlow.asSharedFlow()

    suspend fun showNotification(notification: NotificationItem) {
        _notificationFlow.emit(notification)
    }
}
