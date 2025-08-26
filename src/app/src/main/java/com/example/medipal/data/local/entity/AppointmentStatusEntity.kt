package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medipal.domain.model.AppointmentStatus
import com.example.medipal.domain.model.AppointmentStatusType

@Entity(tableName = "appointment_status")
data class AppointmentStatusEntity(
    @PrimaryKey val id: String,
    val appointmentId: String,
    val profileId: String,
    val scheduledTime: Long,
    val actualTime: Long?,
    val status: String, // Store as String to avoid enum issues
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

// Extension functions for conversion
fun AppointmentStatusEntity.toDomain(): AppointmentStatus {
    return AppointmentStatus(
        id = id,
        appointmentId = appointmentId,
        profileId = profileId,
        scheduledTime = scheduledTime,
        actualTime = actualTime,
        status = AppointmentStatusType.valueOf(status),
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun AppointmentStatus.toEntity(): AppointmentStatusEntity {
    return AppointmentStatusEntity(
        id = id,
        appointmentId = appointmentId,
        profileId = profileId,
        scheduledTime = scheduledTime,
        actualTime = actualTime,
        status = status.name,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
