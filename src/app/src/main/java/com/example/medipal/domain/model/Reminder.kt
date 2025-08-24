package com.example.medipal.domain.model

import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val scheduleTime: Long = 0,
    val notes: String = "",
    val frequency: Frequency = Frequency.EveryDay(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        title = "",
        scheduleTime = 0,
        notes = "",
        frequency = Frequency.EveryDay(),
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}
