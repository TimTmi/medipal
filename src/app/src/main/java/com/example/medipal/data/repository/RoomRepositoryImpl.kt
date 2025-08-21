package com.example.medipal.data.repository

import com.example.medipal.domain.repository.LocalRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

abstract class RoomRepositoryImpl<TDomain, TEntity>(
    private val getAllFlow: () -> Flow<List<TEntity>>,
    private val insert: suspend (TEntity) -> Unit,
    private val update: suspend (TEntity) -> Unit,
    private val deleteById: suspend (String) -> Unit,
    private val toDomain: (TEntity) -> TDomain,
    private val toEntity: (TDomain) -> TEntity
) : LocalRepository<TDomain> {

    override fun getAll(): Flow<List<TDomain>> =
        getAllFlow().map { list -> list.map(toDomain) }

    override suspend fun add(item: TDomain) = insert(toEntity(item))
    override suspend fun remove(id: String) = deleteById(id)
    override suspend fun update(item: TDomain) = update(toEntity(item))
}
