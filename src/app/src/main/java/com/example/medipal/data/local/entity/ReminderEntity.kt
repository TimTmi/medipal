package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val profileId: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: Long = 0,
    val isCompleted: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
