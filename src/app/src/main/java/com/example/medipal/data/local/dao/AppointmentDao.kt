package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointment")
    fun getAll(): Flow<List<AppointmentEntity>>

    @Query("SELECT * FROM appointment")
    suspend fun getAllOnce(): List<AppointmentEntity>

    @Query("SELECT * FROM appointment WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): AppointmentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: AppointmentEntity)

    @Query("DELETE FROM appointment WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: AppointmentEntity)
}
