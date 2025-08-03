package com.example.medipal.domain.model

data class Appointment(
    val id: String,
    val title: String,
    val scheduleTime: Long,
    val doctor: String,
    val notes: String?
)