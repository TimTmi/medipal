package com.example.medipal.domain.model

import java.util.UUID

data class Appointment(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val scheduleTime: Long = 0,
    val doctor: String = "",
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        title = "",
        scheduleTime = 0,
        doctor = "",
        notes = "",
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}