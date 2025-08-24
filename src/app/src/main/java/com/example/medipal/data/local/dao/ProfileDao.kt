package com.example.medipal.data.local.dao

import androidx.room.*
import com.example.medipal.data.local.entity.ProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {

    @Query("SELECT * FROM profile")
    fun getAll(): Flow<List<ProfileEntity>>

    @Query("SELECT * FROM profile")
    suspend fun getAllOnce(): List<ProfileEntity>

    @Query("SELECT * FROM profile WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: ProfileEntity)

    @Query("DELETE FROM profile WHERE id = :id")
    suspend fun deleteById(id: String)

    @Update
    suspend fun update(entity: ProfileEntity)
}
