package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder")
    fun getAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder")
    suspend fun getAllOnce(): List<ReminderEntity>

    @Query("SELECT * FROM reminder WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReminderEntity)

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: ReminderEntity)
}
