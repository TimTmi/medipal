package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.example.medipal.domain.repository.MedicationRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreMedicationRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileId: String
) : FirestoreRepositoryImpl<Medication>(
    firestore.collection("profiles").document(profileId).collection("medications"),
    Medication::class.java,
    setId = { medication, id -> medication.copy(id = id) }
), MedicationRepository {
    override fun getId(item: Medication) = item.id
}
