package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "profile")
data class ProfileEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val fullName: String = "",
    val birthday: Long = 0,
    val height: Float = 0f,
    val weight: Float = 0f,
    val conditions: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
