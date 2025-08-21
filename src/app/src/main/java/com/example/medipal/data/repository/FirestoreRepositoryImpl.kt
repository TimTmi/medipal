package com.example.medipal.data.repository

import com.example.medipal.domain.repository.RemoteRepository
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

abstract class FirestoreRepositoryImpl<T : Any>(
    private val collection: CollectionReference,
    private val setId: (T, String) -> T
) : RemoteRepository<T> {

    override fun getAll(): Flow<List<T>> = callbackFlow {
        val listener = collection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val items = snapshot?.documents?.mapNotNull { doc ->
                doc.toObject(getModelClass())?.let { setId(it, doc.id) }
            } ?: emptyList()

            trySend(items)
        }

        awaitClose { listener.remove() }
    }

    override suspend fun add(item: T) {
        val docRef = collection.document()
        val itemWithId = setId(item, docRef.id)
        docRef.set(itemWithId).await()
    }

    override suspend fun update(item: T) {
        collection.document(getId(item)).set(item).await()
    }

    override suspend fun remove(id: String) {
        collection.document(id).delete().await()
    }

    /** Must return the KClass for Firestore to deserialize */
    protected abstract fun getModelClass(): Class<T>

    /** Must return the item's id for update */
    protected abstract fun getId(item: T): String
}