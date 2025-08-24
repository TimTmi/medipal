package com.example.medipal.data.local.dao

import androidx.room.Dao
import kotlinx.coroutines.flow.Flow

interface BaseDao<TEntity> {
    fun getAll(): Flow<List<TEntity>>
    suspend fun getAllOnce(): List<TEntity>
    suspend fun getByIds(id: String): TEntity?
    suspend fun insert(entity: TEntity)
    suspend fun deleteById(id: String)
    suspend fun update(entity: TEntity)
}
