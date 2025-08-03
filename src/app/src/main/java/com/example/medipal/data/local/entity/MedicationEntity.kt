package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey val id: String,
    val name: String,
    val dosage: String,
    val scheduleTime: Long,
    val notes: String?
)