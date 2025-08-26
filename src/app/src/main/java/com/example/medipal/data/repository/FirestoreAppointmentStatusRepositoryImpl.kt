package com.example.medipal.data.repository

import com.example.medipal.domain.model.AppointmentStatus
import com.example.medipal.domain.repository.AppointmentStatusRepository
import com.example.medipal.util.ProfileRepositoryManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreAppointmentStatusRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileRepositoryManager: ProfileRepositoryManager
) : FirestoreRepositoryImpl<AppointmentStatus>(
    {
        firestore.collection("profiles")
            .document(profileRepositoryManager.getCurrentProfileId())
            .collection("appointment_status")
    },
    AppointmentStatus::class.java,
    setId = { item, id -> item.copy(id = id) }
), AppointmentStatusRepository {
    
    override fun getCollection() = super.getCollection()
    override fun getId(item: AppointmentStatus) = item.id

    override fun getAll(): Flow<List<AppointmentStatus>> = callbackFlow {
        val listener = getCollection().addSnapshotListener { snapshot, _ ->
            val statuses = snapshot?.documents?.mapNotNull { it.toObject(AppointmentStatus::class.java) } ?: emptyList()
            trySend(statuses)
        }
        awaitClose { listener.remove() }
    }

    override suspend fun getAllOnce(): List<AppointmentStatus> {
        val snapshot = getCollection().get().await()
        return snapshot.documents.mapNotNull { it.toObject(AppointmentStatus::class.java) }
    }

    override suspend fun getById(id: String): AppointmentStatus? {
        val snapshot = getCollection().document(id).get().await()
        return snapshot.toObject(AppointmentStatus::class.java)
    }

    override suspend fun add(item: AppointmentStatus) {
        getCollection().document(item.id).set(item).await()
    }

    override suspend fun remove(id: String) {
        getCollection().document(id).delete().await()
    }

    override suspend fun update(item: AppointmentStatus) {
        getCollection().document(item.id).set(item).await()
    }

    override suspend fun getByAppointmentAndTime(appointmentId: String, scheduledTime: Long): AppointmentStatus? {
        val snapshot = getCollection()
            .whereEqualTo("appointmentId", appointmentId)
            .whereEqualTo("scheduledTime", scheduledTime)
            .limit(1)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.toObject(AppointmentStatus::class.java)
    }

    override fun getByAppointmentId(appointmentId: String): Flow<List<AppointmentStatus>> = callbackFlow {
        val listener = getCollection()
            .whereEqualTo("appointmentId", appointmentId)
            .addSnapshotListener { snapshot, _ ->
                val statuses = snapshot?.documents?.mapNotNull { it.toObject(AppointmentStatus::class.java) } ?: emptyList()
                trySend(statuses)
            }
        awaitClose { listener.remove() }
    }
}
