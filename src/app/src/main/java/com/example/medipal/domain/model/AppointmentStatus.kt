package com.example.medipal.domain.model

import java.util.UUID

enum class AppointmentStatusType {
    ATTENDED,
    MISSED,
    CANCELLED
}

data class AppointmentStatus(
    val id: String = UUID.randomUUID().toString(),
    val appointmentId: String = "",
    val profileId: String = "",
    val scheduledTime: Long = 0,
    val actualTime: Long? = null, // When status was recorded
    val status: AppointmentStatusType = AppointmentStatusType.MISSED,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        appointmentId = "",
        profileId = "",
        scheduledTime = 0,
        actualTime = null,
        status = AppointmentStatusType.MISSED,
        notes = "",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}
