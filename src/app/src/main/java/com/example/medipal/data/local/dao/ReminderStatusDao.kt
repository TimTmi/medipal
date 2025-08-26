package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.ReminderStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderStatusDao {

    @Query("SELECT * FROM reminder_status WHERE profileId = :profileId")
    fun getAllByProfileId(profileId: String): Flow<List<ReminderStatusEntity>>

    @Query("SELECT * FROM reminder_status WHERE profileId = :profileId")
    suspend fun getAllOnceByProfileId(profileId: String): List<ReminderStatusEntity>

    @Query("SELECT * FROM reminder_status WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ReminderStatusEntity?

    @Query("SELECT * FROM reminder_status WHERE reminderId = :reminderId AND profileId = :profileId")
    fun getByReminderIdAndProfileId(reminderId: String, profileId: String): Flow<List<ReminderStatusEntity>>

    @Query("SELECT * FROM reminder_status WHERE reminderId = :reminderId AND scheduledTime = :scheduledTime AND profileId = :profileId LIMIT 1")
    suspend fun getByReminderAndTimeAndProfileId(
        reminderId: String,
        scheduledTime: Long,
        profileId: String
    ): ReminderStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ReminderStatusEntity)

    @Query("DELETE FROM reminder_status WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: ReminderStatusEntity)

    // Legacy methods for backward compatibility
    @Query("SELECT * FROM reminder_status")
    fun getAll(): Flow<List<ReminderStatusEntity>>

    @Query("SELECT * FROM reminder_status")
    suspend fun getAllOnce(): List<ReminderStatusEntity>
}
