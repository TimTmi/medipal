package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder WHERE profileId = :profileId")
    fun getAllByProfileId(profileId: String): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder WHERE profileId = :profileId")
    suspend fun getAllOnceByProfileId(profileId: String): List<ReminderEntity>

    @Query("SELECT * FROM reminder WHERE id = :id AND profileId = :profileId LIMIT 1")
    suspend fun getByIdAndProfileId(id: String, profileId: String): ReminderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReminderEntity)

    @Query("DELETE FROM reminder WHERE id = :id AND profileId = :profileId")
    suspend fun deleteByIdAndProfileId(id: String, profileId: String)

    @Update
    suspend fun update(entity: ReminderEntity)

    // Legacy methods for backward compatibility
    @Query("SELECT * FROM reminder")
    fun getAll(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder")
    suspend fun getAllOnce(): List<ReminderEntity>

    @Query("SELECT * FROM reminder WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ReminderEntity?

    @Query("DELETE FROM reminder WHERE id = :id")
    suspend fun deleteById(id: String)
}
