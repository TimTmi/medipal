package com.example.medipal.domain.model

import java.util.UUID

data class Profile (
    val id: String = UUID.randomUUID().toString(),
    val fullName: String = "",
    val birthday: Long = 0, // timestamp
    val height: Float = 0f,
    val weight: Float = 0f,
    val conditions: String = "",
    val avatarUrl: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        fullName = "",
        birthday = 0,
        height = 0f,
        weight = 0f,
        conditions = "",
        avatarUrl = "",
        updatedAt = System.currentTimeMillis(),
        deletedAt = null
    )
}