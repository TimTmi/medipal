package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medipal.domain.model.ReminderStatus
import com.example.medipal.domain.model.ReminderStatusType

@Entity(tableName = "reminder_status")
data class ReminderStatusEntity(
    @PrimaryKey val id: String,
    val reminderId: String,
    val profileId: String,
    val scheduledTime: Long,
    val actualTime: Long?,
    val status: String, // Store as String to avoid enum issues
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

// Extension functions for conversion
fun ReminderStatusEntity.toDomain(): ReminderStatus {
    return ReminderStatus(
        id = id,
        reminderId = reminderId,
        profileId = profileId,
        scheduledTime = scheduledTime,
        actualTime = actualTime,
        status = ReminderStatusType.valueOf(status),
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun ReminderStatus.toEntity(): ReminderStatusEntity {
    return ReminderStatusEntity(
        id = id,
        reminderId = reminderId,
        profileId = profileId,
        scheduledTime = scheduledTime,
        actualTime = actualTime,
        status = status.name,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
