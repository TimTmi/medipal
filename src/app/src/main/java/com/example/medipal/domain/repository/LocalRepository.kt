package com.example.medipal.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalRepository<T> : Repository<T> {
//    suspend fun getPendingSync(): List<T>
}