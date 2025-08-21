package com.example.medipal.domain.repository

import kotlinx.coroutines.flow.Flow

interface RemoteRepository<T> : Repository<T> {
    override fun getAll(): Flow<List<T>>
    override suspend fun add(item: T)
    override suspend fun remove(id: String)
    override suspend fun update(item: T)
}