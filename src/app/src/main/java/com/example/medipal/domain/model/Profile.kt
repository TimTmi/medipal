package com.example.medipal.domain.model

import java.util.UUID

data class Profile (
    val id: String = UUID.randomUUID().toString(),
    val fullName: String = "",
    val birthday: Long = 0,
    val height: Float = 0f,
    val weight: Float = 0f
)