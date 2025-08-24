package com.example.medipal.domain.model

import java.util.UUID

data class Appointment(
    val id: String = UUID.randomUUID().toString(),
    val profileId: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: Long = 0,
    val location: String = "",
    val doctorName: String = "",
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
        location = "",
        doctorName = "",
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}