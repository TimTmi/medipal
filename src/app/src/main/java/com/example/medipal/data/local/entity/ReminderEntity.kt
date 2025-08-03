package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val scheduleTime: Long,
    val notes: String?
)
