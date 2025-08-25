package com.example.medipal.data.repository

import com.example.medipal.domain.model.MedicationDose
import com.example.medipal.domain.repository.MedicationDoseRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class FirestoreMedicationDoseRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val profileId: String
) : MedicationDoseRepository {

    private val collection = firestore.collection("medication_doses")

    override fun getAll(): Flow<List<MedicationDose>> = flow {
        try {
            val snapshot = collection
                .whereEqualTo("profileId", profileId)
                .get()
                .await()
            val doses = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MedicationDose::class.java)
            }
            emit(doses)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getAllOnce(): List<MedicationDose> {
        return try {
            val snapshot = collection
                .whereEqualTo("profileId", profileId)
                .get()
                .await()
            snapshot.documents.mapNotNull { doc ->
                doc.toObject(MedicationDose::class.java)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getById(id: String): MedicationDose? {
        return try {
            val doc = collection.document(id).get().await()
            doc.toObject(MedicationDose::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun add(item: MedicationDose) {
        try {
            collection.document(item.id).set(item).await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun remove(id: String) {
        try {
            collection.document(id).delete().await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    override suspend fun update(item: MedicationDose) {
        try {
            collection.document(item.id).set(item).await()
        } catch (e: Exception) {
            // Handle error
        }
    }

    override fun getByMedicationId(medicationId: String): Flow<List<MedicationDose>> = flow {
        try {
            val snapshot = collection
                .whereEqualTo("medicationId", medicationId)
                .get()
                .await()
            val doses = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MedicationDose::class.java)
            }
            emit(doses)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }

    override suspend fun getByMedicationAndTime(medicationId: String, scheduledTime: Long): MedicationDose? {
        return try {
            val snapshot = collection
                .whereEqualTo("medicationId", medicationId)
                .whereEqualTo("scheduledTime", scheduledTime)
                .limit(1)
                .get()
                .await()
            snapshot.documents.firstOrNull()?.toObject(MedicationDose::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override fun getByProfileId(profileId: String): Flow<List<MedicationDose>> = flow {
        try {
            val snapshot = collection
                .whereEqualTo("profileId", profileId)
                .get()
                .await()
            val doses = snapshot.documents.mapNotNull { doc ->
                doc.toObject(MedicationDose::class.java)
            }
            emit(doses)
        } catch (e: Exception) {
            emit(emptyList())
        }
    }
}
