package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medication WHERE profileId = :profileId")
    fun getAllByProfileId(profileId: String): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medication WHERE profileId = :profileId")
    suspend fun getAllOnceByProfileId(profileId: String): List<MedicationEntity>

    @Query("SELECT * FROM medication WHERE id = :id AND profileId = :profileId LIMIT 1")
    suspend fun getByIdAndProfileId(id: String, profileId: String): MedicationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: MedicationEntity)

    @Query("DELETE FROM medication WHERE id = :id AND profileId = :profileId")
    suspend fun deleteByIdAndProfileId(id: String, profileId: String)

    @Update
    suspend fun update(entity: MedicationEntity)

    // Legacy methods for backward compatibility
    @Query("SELECT * FROM medication")
    fun getAll(): Flow<List<MedicationEntity>>

    @Query("SELECT * FROM medication")
    suspend fun getAllOnce(): List<MedicationEntity>

    @Query("SELECT * FROM medication WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): MedicationEntity?

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteById(id: String)
}