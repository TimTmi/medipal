package com.example.medipal.data.repository

import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import com.example.medipal.util.ProfileRepositoryManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreMedicationDoseRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileRepositoryManager: ProfileRepositoryManager
) : FirestoreRepositoryImpl<MedicationDose>(
    {
        firestore.collection("profiles")
            .document(profileRepositoryManager
                .getCurrentProfileId())
            .collection("medication_doses")
    },
    MedicationDose::class.java,
    setId = { item, id -> item.copy(id = id) }
), MedicationDoseRepository {
    override fun getCollection() = super.getCollection()
    override fun getId(item: MedicationDose) = item.id

    override fun getByMedicationId(medicationId: String): Flow<List<MedicationDose>> = callbackFlow {
        val query = getCollection().whereEqualTo("medicationId", medicationId)
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            val doses = snapshot?.documents?.mapNotNull { it.toObject(MedicationDose::class.java) } ?: emptyList()
            trySend(doses)
        }
        awaitClose { listener.remove() }
    }


    override suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose? {
        val snapshot = getCollection()
            .whereEqualTo("medicationId", medicationId)
            .whereEqualTo("scheduledTime", scheduledTime)
            .limit(1)
            .get()
            .await()
        return snapshot.documents.firstOrNull()?.toObject(MedicationDose::class.java)
    }
}
