package com.example.medipal.data.repository

import com.example.medipal.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class RoomRepositoryImpl<TDomain, TEntity>(
    private val getAllFlow: () -> Flow<List<TEntity>>,
    private val getAllOnceFunc: suspend () -> List<TEntity>,
    private val getByIdFunc: suspend (String) -> TEntity?,
    private val insert: suspend (TEntity) -> Unit,
    private val updateFunc: suspend (TEntity) -> Unit,
    private val deleteById: suspend (String) -> Unit,
    protected val toDomain: (TEntity) -> TDomain,
    protected val toEntity: (TDomain) -> TEntity,
    protected val getId: (TDomain) -> String
) : LocalRepository<TDomain> {

    override fun getAll(): Flow<List<TDomain>> =
        getAllFlow().map { list -> list.map(toDomain) }

    override suspend fun getAllOnce(): List<TDomain> =
        getAllOnceFunc().map(toDomain)

    override suspend fun getById(id: String): TDomain? =
        getByIdFunc(id)?.let(toDomain)

    override suspend fun add(item: TDomain) {
        insert(toEntity(item))
    }

    override suspend fun update(item: TDomain) {
        updateFunc(toEntity(item))
    }

    override suspend fun remove(id: String) {
        deleteById(id)
    }
}

