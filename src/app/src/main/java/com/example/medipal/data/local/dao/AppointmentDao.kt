package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.AppointmentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppointmentDao : BaseDao<AppointmentEntity> {

    @Query("SELECT * FROM appointment")
    override fun getAll(): Flow<List<AppointmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: AppointmentEntity)

    @Query("DELETE FROM appointment WHERE id = :id")
    override suspend fun deleteById(id: String)

    @Update
    override suspend fun update(entity: AppointmentEntity)
}
