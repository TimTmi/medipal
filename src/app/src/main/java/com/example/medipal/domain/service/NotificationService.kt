package com.example.medipal.domain.service

import com.example.medipal.domain.model.Reminder

interface NotificationService {
    fun scheduleNotification(reminder: Reminder)
    fun cancelNotification(id: String)
}