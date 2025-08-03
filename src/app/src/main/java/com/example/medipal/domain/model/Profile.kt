package com.example.medipal.domain.model

import java.time.LocalDate

data class Profile (
    val id: String,
    val fullName: String,
    val birthday: LocalDate,
    val height: Float,
    val weight: Float
)