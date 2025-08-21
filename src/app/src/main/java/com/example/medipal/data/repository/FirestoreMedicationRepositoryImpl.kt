package com.example.medipal.data.repository

import com.example.medipal.domain.model.Medication
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreMedicationRepositoryImpl(
    firestore: FirebaseFirestore
) : FirestoreRepositoryImpl<Medication>(
    firestore.collection("medications"),
    setId = { med, id -> med.copy(id = id) }
) {
    override fun getModelClass() = Medication::class.java
    override fun getId(item: Medication) = item.id
}
