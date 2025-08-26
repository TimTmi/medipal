package com.example.medipal.data.repository

import com.example.medipal.domain.model.ReminderStatus
import com.example.medipal.domain.repository.ReminderStatusRepository
import com.example.medipal.util.ProfileRepositoryManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreReminderStatusRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileRepositoryManager: ProfileRepositoryManager
) : FirestoreRepositoryImpl<ReminderStatus>(
    {
        firestore.collection("profiles")
            .document(profileRepositoryManager.getCurrentProfileId())
            .collection("reminder_status")
    },
    ReminderStatus::class.java,
    setId = { item, id -> item.copy(id = id) }
), ReminderStatusRepository {
    
    override fun getCollection() = super.getCollection()
    override fun getId(item: ReminderStatus) = item.id

    override fun getAll(): Flow<List<ReminderStatus>> = callbackFlow {
        val listener = getCollection().addSnapshotListener { snapshot, _ ->
            val statuses = snapshot?.documents?.mapNotNull { it.toObject(ReminderStatus::class.java) } ?: emptyList()
            trySend(statuses)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getAllOnce(): List<ReminderStatus> {
        val snapshot = getCollection().get().await()
        return snapshot.documents.mapNotNull { it.toObject(ReminderStatus::class.java) }
    }

    override suspend fun getById(id: String): ReminderStatus? {
        val snapshot = getCollection().document(id).get().await()
        return snapshot.toObject(ReminderStatus::class.java)
    }

    override suspend fun add(item: ReminderStatus) {
        getCollection().document(item.id).set(item).await()
    }

    override suspend fun remove(id: String) {
        getCollection().document(id).delete().await()
    }

    override suspend fun update(item: ReminderStatus) {
        getCollection().document(item.id).set(item).await()
    }

    override suspend fun getByReminderAndTime(reminderId: String, scheduledTime: Long): ReminderStatus? {
        val snapshot = getCollection()
            .whereEqualTo("reminderId", reminderId)
            .whereEqualTo("scheduledTime", scheduledTime)
            .limit(1)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.toObject(ReminderStatus::class.java)
    }

    override fun getByReminderId(reminderId: String): Flow<List<ReminderStatus>> = callbackFlow {
        val listener = getCollection()
            .whereEqualTo("reminderId", reminderId)
            .addSnapshotListener { snapshot, _ ->
                val statuses = snapshot?.documents?.mapNotNull { it.toObject(ReminderStatus::class.java) } ?: emptyList()
                trySend(statuses)
            }
        awaitClose { listener.remove() }
    }
}
