package com.example.medipal.domain.model

import java.util.UUID

data class Medication(
    val id: String = UUID.randomUUID().toString(),
    val profileId: String = "",
    val name: String = "",
    val dosage: String = "",
    val scheduleTime: Long = 0,
    val description: String = "",
    val frequency: Frequency = Frequency.EveryDay(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        profileId = "",
        name = "",
        dosage = "",
        scheduleTime = 0,
        description = "",
        frequency = Frequency.EveryDay(),
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}