package com.example.medipal.domain.model

import java.time.LocalDate

data class Profile (
    val id: String = "",
    val fullName: String = "",
    val birthday: Long = 0,
    val height: Float = 0f,
    val weight: Float = 0f
)