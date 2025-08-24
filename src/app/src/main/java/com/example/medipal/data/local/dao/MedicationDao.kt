package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medication")
    fun getAll(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medication")
    suspend fun getAllOnce(): List<MedicationEntity>

    @Query("SELECT * FROM medication WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): MedicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MedicationEntity)

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: MedicationEntity)
}