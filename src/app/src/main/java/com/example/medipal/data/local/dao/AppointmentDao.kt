package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointment WHERE profileId = :profileId")
    fun getAllByProfileId(profileId: String): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointment WHERE profileId = :profileId")
    suspend fun getAllOnceByProfileId(profileId: String): List<AppointmentEntity>

    @Query("SELECT * FROM appointment WHERE id = :id AND profileId = :profileId LIMIT 1")
    suspend fun getByIdAndProfileId(id: String, profileId: String): AppointmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AppointmentEntity)

    @Query("DELETE FROM appointment WHERE id = :id AND profileId = :profileId")
    suspend fun deleteByIdAndProfileId(id: String, profileId: String)

    @Update
    suspend fun update(entity: AppointmentEntity)

    // Legacy methods for backward compatibility
    @Query("SELECT * FROM appointment")
    fun getAll(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointment")
    suspend fun getAllOnce(): List<AppointmentEntity>

    @Query("SELECT * FROM appointment WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): AppointmentEntity?

    @Query("DELETE FROM appointment WHERE id = :id")
    suspend fun deleteById(id: String)
}
