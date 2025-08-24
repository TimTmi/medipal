package com.example.medipal.domain.model

import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val profileId: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: Long = 0,
    val isCompleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        profileId = "",
        title = "",
        description = "",
        dateTime = 0,
        isCompleted = false,
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}
