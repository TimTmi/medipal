package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao {

    @Query("SELECT * FROM medication")
    fun getAll(): Flow<List<MedicationEntity>>
    @Query("SELECT * FROM medication WHERE id = :id")
    fun getMedicationById(id: String): Flow<MedicationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medication: MedicationEntity)

    @Delete
    suspend fun delete(medication: MedicationEntity)

    @Update
    suspend fun update(medication: MedicationEntity)

    @Query("DELETE FROM medication WHERE id = :id")
    suspend fun deleteById(id: String)

}