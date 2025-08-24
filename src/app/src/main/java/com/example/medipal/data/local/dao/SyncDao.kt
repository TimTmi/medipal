package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.SyncEntity

@Dao
interface SyncDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SyncEntity)

    @Query("SELECT id FROM sync_table WHERE entityType = :type AND isDeleted = 0")
    suspend fun getPendingForType(type: String): List<String>

    @Query("SELECT id FROM sync_table WHERE entityType = :type AND isDeleted = 1")
    suspend fun getDeletedForType(type: String): List<String>
}
