package com.example.medipal.domain.model

data class Reminder(
    val id: String,
    val title: String,
    val scheduleTime: Long,
    val notes: String?
)
