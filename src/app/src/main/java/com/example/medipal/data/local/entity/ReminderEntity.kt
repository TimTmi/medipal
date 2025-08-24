package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val scheduleTime: Long = 0,
    val notes: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
