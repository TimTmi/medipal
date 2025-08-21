package com.example.medipal.data.local.dao

import kotlinx.coroutines.flow.Flow

interface BaseDao<TEntity> {
    fun getAll(): Flow<List<TEntity>>
    suspend fun insert(entity: TEntity)
    suspend fun deleteById(id: String)
    suspend fun update(entity: TEntity)
}
