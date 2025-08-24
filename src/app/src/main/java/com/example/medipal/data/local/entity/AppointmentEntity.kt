package com.example.medipal.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "appointment")
data class AppointmentEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val profileId: String = "",
    val title: String = "",
    val description: String = "",
    val dateTime: Long = 0,
    val location: String = "",
    val doctorName: String = "",
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
)
