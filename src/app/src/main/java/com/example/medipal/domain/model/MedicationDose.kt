package com.example.medipal.domain.model

import java.util.UUID

enum class DoseStatus {
    TAKEN,
    SKIPPED,
    MISSED
}

data class MedicationDose(
    val id: String = UUID.randomUUID().toString(),
    val medicationId: String = "",
    val profileId: String = "",
    val scheduledTime: Long = 0,
    val actualTime: Long? = null, // When it was actually taken
    val status: DoseStatus = DoseStatus.MISSED,
    val notes: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        medicationId = "",
        profileId = "",
        scheduledTime = 0,
        actualTime = null,
        status = DoseStatus.MISSED,
        notes = "",
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}
