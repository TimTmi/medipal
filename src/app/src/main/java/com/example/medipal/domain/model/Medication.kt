package com.example.medipal.domain.model

import java.util.UUID

data class Medication(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val dosage: String = "",
    val scheduleTime: Long = 0,
    val notes: String = "",
    val frequency: Frequency = Frequency.EveryDay(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        name = "",
        dosage = "",
        scheduleTime = 0,
        notes = "",
        frequency = Frequency.EveryDay(),
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}