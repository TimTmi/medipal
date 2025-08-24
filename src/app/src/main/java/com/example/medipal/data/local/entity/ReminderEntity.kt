package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medipal.domain.model.Frequency

@Entity(tableName = "reminder")
data class ReminderEntity(
    @PrimaryKey val id: String,
    val title: String,
    val scheduleTime: Long,
    val notes: String?,
    val frequency: Frequency
)
