package com.example.medipal.data.repository

//import com.example.medipal.domain.model.Medication
//import com.example.medipal.domain.repository.MedicationRepository
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.tasks.await
//
//class FirestoreMedicationRepositoryImpl(
//    private val firestore: FirebaseFirestore
//) : MedicationRepository {
//
//    private val medsCollection = firestore.collection("medications")
//
//    override fun getMedications(): Flow<List<Medication>> = callbackFlow {
//        val listener = medsCollection.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                close(error)
//                return@addSnapshotListener
//            }
//
//            val meds = snapshot?.documents?.mapNotNull { doc ->
//                doc.toObject(Medication::class.java)?.copy(id = doc.id)
//            } ?: emptyList()
//
//            trySend(meds)
//        }
//
//        awaitClose { listener.remove() }
//    }
//
//    override suspend fun addMedication(medication: Medication) {
//        val newDoc = medsCollection.document()
//        val medWithId = medication.copy(id = newDoc.id)
//        newDoc.set(medWithId).await()
//    }
//
//    override suspend fun removeMedication(id: String) {
//        medsCollection.document(id).delete().await()
//    }
//
//    override suspend fun updateMedication(medication: Medication) {
//        medsCollection.document(medication.id).set(medication).await()
//    }
//}
