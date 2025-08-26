package com.example.medipal.data.repository

import com.example.medipal.domain.repository.LocalRepository
import com.example.medipal.domain.repository.RemoteRepository
import com.example.medipal.domain.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

open class HybridRepositoryImpl<T>(
    protected val localRepo: LocalRepository<T>,
    private val remoteRepo: RemoteRepository<T>,
    private val networkChecker: () -> Boolean,
    private val getId: (T) -> String,
    private val getUpdatedAt: (T) -> Long,
    private val getDeletedAt: (T) -> Long?,
    private val copyWithUpdated: (T, Long) -> T,
    private val copyWithDeleted: (T, Long?) -> T
) : Repository<T> {

    override fun getAll(): Flow<List<T>> =
        localRepo.getAll().map { list -> list.filter { getDeletedAt(it) == null } }

    override suspend fun getAllOnce(): List<T> =
        localRepo.getAllOnce().filter { getDeletedAt(it) == null }

    override suspend fun getById(id: String): T? =
        localRepo.getById(id)?.takeIf { getDeletedAt(it) == null }

    override suspend fun add(item: T) {
        localRepo.add(item)
        syncItem(item)
    }

    override suspend fun remove(id: String) {
        val item = localRepo.getById(id) ?: return
        val deletedItem = copyWithDeleted(item, System.currentTimeMillis())
        localRepo.update(deletedItem)
        syncItem(deletedItem)
    }

    override suspend fun update(item: T) {
        val updatedItem = copyWithUpdated(item, System.currentTimeMillis())
        localRepo.update(updatedItem)
        syncItem(updatedItem)
    }

    private suspend fun syncItem(item: T) {
        if (!networkChecker()) return

        val remoteItem = remoteRepo.getById(getId(item))

        when {
            remoteItem == null && getDeletedAt(item) == null -> remoteRepo.add(item)
            remoteItem != null -> {
                val localDel = getDeletedAt(item)
                val remoteDel = getDeletedAt(remoteItem)
                val localUpdated = getUpdatedAt(item)
                val remoteUpdated = getUpdatedAt(remoteItem)

                // If local deleted
                if (localDel != null) {
                    if (remoteDel == null || localDel > remoteDel) {
                        remoteRepo.update(item) // propagate deletion
                    }
                } else if (remoteDel != null) {
                    if (remoteDel > localUpdated) {
                        localRepo.update(remoteItem) // propagate remote deletion
                    }
                } else {
                    // No deletions, pick latest updated
                    if (localUpdated >= remoteUpdated) remoteRepo.update(item)
                    else localRepo.update(remoteItem)
                }
            }
        }
    }

    suspend fun syncAll() {
        if (!networkChecker()) return

        val localItems = localRepo.getAllOnce()
        val remoteItems = remoteRepo.getAllOnce()

        val localMap = localItems.associateBy(getId)
        val remoteMap = remoteItems.associateBy(getId)

        val allIds = localMap.keys + remoteMap.keys

        for (id in allIds) {
            val local = localMap[id]
            val remote = remoteMap[id]

            when {
                local == null && remote != null && getDeletedAt(remote) == null -> localRepo.add(remote)
                local != null && remote == null && getDeletedAt(local) == null -> remoteRepo.add(local)
                local != null && remote != null -> {
                    val localDel = getDeletedAt(local)
                    val remoteDel = getDeletedAt(remote)
                    val localUpdated = getUpdatedAt(local)
                    val remoteUpdated = getUpdatedAt(remote)

                    when {
                        localDel != null && (remoteDel == null || localDel > remoteDel) ->
                            remoteRepo.update(local)
                        remoteDel != null && (localDel == null || remoteDel > localDel) ->
                            localRepo.update(remote)
                        localUpdated > remoteUpdated -> remoteRepo.update(local)
                        remoteUpdated > localUpdated -> localRepo.update(remote)
                    }
                }
            }
        }
    }

    /** Listen to remote changes and update Room accordingly */
    fun startRemoteListener(scope: CoroutineScope) {
        if (remoteRepo !is FirestoreRepositoryImpl<*>) return

        remoteRepo.getCollection().addSnapshotListener { snapshot, error ->
            if (error != null) return@addSnapshotListener
            scope.launch { syncAll() }
        }
    }
}
