package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao : BaseDao<ReminderEntity> {

    @Query("SELECT * FROM reminder")
    override fun getAll(): Flow<List<ReminderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(entity: ReminderEntity)

    @Query("DELETE FROM reminder WHERE id = :id")
    override suspend fun deleteById(id: String)

    @Update
    override suspend fun update(entity: ReminderEntity)
}
