package com.example.medipal.domain.model

data class Medication(
    val id: String = "",
    val name: String = "",
    val dosage: String = "",
    val scheduleTime: Long = 0,
    val notes: String = ""
)