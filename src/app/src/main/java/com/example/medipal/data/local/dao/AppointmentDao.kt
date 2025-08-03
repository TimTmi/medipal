package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM appointment")
    fun getAll(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: AppointmentEntity)

    @Update
    suspend fun update(appointment: AppointmentEntity)

    @Delete
    suspend fun delete(appointment: AppointmentEntity)

    @Query("DELETE FROM appointment WHERE id = :id")
    suspend fun deleteById(id: String)
}
