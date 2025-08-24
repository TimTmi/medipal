package com.example.medipal.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "sync_table",
    primaryKeys = ["id", "entityType"]
)
data class SyncEntity(
    val id: String,
    val entityType: String,
    val lastModified: Long = System.currentTimeMillis(),
    val isDeleted: Boolean = false
)