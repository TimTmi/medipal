package com.example.medipal.domain.model

import java.util.UUID

enum class ReminderStatusType {
    COMPLETED,
    MISSED,
    SKIPPED,
    SNOOZED
}

data class ReminderStatus(
    val id: String = UUID.randomUUID().toString(),
    val reminderId: String = "",
    val profileId: String = "",
    val scheduledTime: Long = 0,
    val actualTime: Long? = null, // When status was recorded
    val status: ReminderStatusType = ReminderStatusType.MISSED,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        reminderId = "",
        profileId = "",
        scheduledTime = 0,
        actualTime = null,
        status = ReminderStatusType.MISSED,
        notes = "",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}
