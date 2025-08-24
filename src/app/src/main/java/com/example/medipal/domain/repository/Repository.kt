package com.example.medipal.domain.repository

import kotlinx.coroutines.flow.Flow

interface Repository<T> {
    fun getAll(): Flow<List<T>>
    suspend fun getAllOnce(): List<T>
    suspend fun getById(id: String): T?
    suspend fun add(item: T)
    suspend fun remove(id: String)
    suspend fun update(item: T)
}