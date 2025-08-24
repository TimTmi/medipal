package com.example.medipal.data.repository

import com.example.medipal.domain.repository.RemoteRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.serializer

abstract class FirestoreRepositoryImpl<T : Any>(
    private val collection: CollectionReference,
    private val modelClass: Class<T>,
    private val setId: (T, String) -> T
) : RemoteRepository<T> {

    override fun getAll(): Flow<List<T>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val items = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(modelClass)?.let { setId(it, doc.id) }
            } ?: emptyList()

            trySend(items)
        }

        awaitClose { listener.remove() }
    }

    override suspend fun getAllOnce(): List<T> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { doc ->
            doc.toObject(modelClass)?.let { setId(it, doc.id) }
        }
    }

    override suspend fun getById(id: String): T? {
        val snapshot = collection.document(id).get().await()
        return snapshot.toObject(modelClass)?.let { setId(it, snapshot.id) }
    }

    override suspend fun add(item: T) {
        val id = getId(item) // use the domain ID
        val itemWithId = setId(item, id)
        collection.document(id).set(itemWithId).await() // suspend until write finishes
    }

    override suspend fun update(item: T) {
        collection.document(getId(item)).set(item).await()
    }

    override suspend fun remove(id: String) {
        collection.document(id).delete().await()
    }

    override fun getCollection() = collection

    /** Must return the item's id for update */
    protected abstract fun getId(item: T): String
}