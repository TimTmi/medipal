package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.AppointmentStatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentStatusDao {

    @Query("SELECT * FROM appointment_status WHERE profileId = :profileId")
    fun getAllByProfileId(profileId: String): Flow<List<AppointmentStatusEntity>>

    @Query("SELECT * FROM appointment_status WHERE profileId = :profileId")
    suspend fun getAllOnceByProfileId(profileId: String): List<AppointmentStatusEntity>

    @Query("SELECT * FROM appointment_status WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): AppointmentStatusEntity?

    @Query("SELECT * FROM appointment_status WHERE appointmentId = :appointmentId AND profileId = :profileId")
    fun getByAppointmentIdAndProfileId(appointmentId: String, profileId: String): Flow<List<AppointmentStatusEntity>>

    @Query("SELECT * FROM appointment_status WHERE appointmentId = :appointmentId AND scheduledTime = :scheduledTime AND profileId = :profileId LIMIT 1")
    suspend fun getByAppointmentAndTimeAndProfileId(
        appointmentId: String,
        scheduledTime: Long,
        profileId: String
    ): AppointmentStatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AppointmentStatusEntity)

    @Query("DELETE FROM appointment_status WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: AppointmentStatusEntity)

    // Legacy methods for backward compatibility
    @Query("SELECT * FROM appointment_status")
    fun getAll(): Flow<List<AppointmentStatusEntity>>

    @Query("SELECT * FROM appointment_status")
    suspend fun getAllOnce(): List<AppointmentStatusEntity>
}
