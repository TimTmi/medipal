package com.example.medipal.domain.model

import java.util.UUID

data class Reminder(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "",
    val scheduleTime: Long = 0,
    val notes: String = "",
    val frequency: Frequency,
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
