package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicationDao : BaseDao<MedicationEntity> {

    @Query("SELECT * FROM medication")
    override fun getAll(): Flow<List<MedicationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: MedicationEntity)

    @Query("DELETE FROM medication WHERE id = :id")
    override suspend fun deleteById(id: String)

    @Update
    override suspend fun update(entity: MedicationEntity)
}