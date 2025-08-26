package com.example.medipal.data.repository

import com.example.medipal.domain.model.CaregiverAssignment
import com.example.medipal.domain.repository.CaregiverAssignmentRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreCaregiverAssignmentRepositoryImpl(
    private val firestore: FirebaseFirestore
) : CaregiverAssignmentRepository {

    private fun collection() = firestore.collection("caregiver_assignments")

    override fun getAssignmentsForCaregiver(caregiverId: String): Flow<List<CaregiverAssignment>> =
        callbackFlow {
            val listener = collection()
                .whereEqualTo("caregiverId", caregiverId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                    } else {
                        val result = snapshot?.documents?.mapNotNull {
                            it.toObject(CaregiverAssignment::class.java)
                        } ?: emptyList()
                        trySend(result)
                    }
                }
            awaitClose { listener.remove() }
        }

    override fun getAssignmentsForCustomer(customerId: String): Flow<List<CaregiverAssignment>> =
        callbackFlow {
            val listener = collection()
                .whereEqualTo("customerId", customerId)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                    } else {
                        val result = snapshot?.documents?.mapNotNull {
                            it.toObject(CaregiverAssignment::class.java)
                        } ?: emptyList()
                        trySend(result)
                    }
                }
            awaitClose { listener.remove() }
        }

    override suspend fun addAssignment(assignment: CaregiverAssignment) {
        collection()
            .add(assignment)
            .await()
    }

    override suspend fun removeAssignment(caregiverId: String, customerId: String) {
        val snapshot = collection()
            .whereEqualTo("caregiverId", caregiverId)
            .whereEqualTo("customerId", customerId)
            .get()
            .await()

        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
    }
}
