package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.medipal.domain.model.Frequency


@Entity(tableName = "medication")
data class MedicationEntity(
    @PrimaryKey val id: String = "",
    val name: String = "",
    val dosage: String = "",
    val scheduleTime: Long = 0,
    val notes: String = "",
    val frequency: Frequency,
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)