package com.example.medipal.domain.model

enum class NotificationStatus {
    MISSED,
    UPCOMING,
    TAKEN,
    SKIPPED
}

enum class NotificationType {
    MEDICATION,
    APPOINTMENT,
    REMINDER
}

data class NotificationItem(
    val id: String,
    val title: String,
    val subtitle: String, // e.g., "Paracetamol Dose" or "Health Check Up"
    val time: String, // formatted time like "7:00 AM"
    val scheduleTime: Long, // timestamp for comparison
    val status: NotificationStatus,
    val type: NotificationType,
    val instructions: String = "", // for medications
    val doctorName: String = "", // for appointments
    val originalItem: Any? = null // stores original Medication/Appointment/Reminder object
)
