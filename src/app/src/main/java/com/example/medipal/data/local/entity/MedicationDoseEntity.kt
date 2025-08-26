package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medipal.domain.model.DoseStatus
import com.example.medipal.domain.model.MedicationDose

@Entity(tableName = "medication_dose")
data class MedicationDoseEntity(
    @PrimaryKey val id: String,
    val medicationId: String,
    val profileId: String,
    val scheduledTime: Long,
    val actualTime: Long?,
    val status: String, // Store as String to avoid enum issues
    val notes: String,
    val createdAt: Long,
    val updatedAt: Long
)

// Extension functions for conversion
fun MedicationDoseEntity.toDomain(): MedicationDose {
    return MedicationDose(
        id = id,
        medicationId = medicationId,
        profileId = profileId,
        scheduledTime = scheduledTime,
        actualTime = actualTime,
        status = DoseStatus.valueOf(status),
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun MedicationDose.toEntity(): MedicationDoseEntity {
    return MedicationDoseEntity(
        id = id,
        medicationId = medicationId,
        profileId = profileId,
        scheduledTime = scheduledTime,
        actualTime = actualTime,
        status = status.name,
        notes = notes,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
