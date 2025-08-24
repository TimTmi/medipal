package com.example.medipal.domain.model

import java.util.UUID

data class Medication(
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val dosage: String = "",
    val scheduleTime: Long = 0,
    val notes: String = "",
    val frequency: Frequency,
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)