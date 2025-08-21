package com.example.medipal.data.repository

import com.example.medipal.domain.model.Appointment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreAppointmentRepositoryImpl(
    firestore: FirebaseFirestore
) : FirestoreRepositoryImpl<Appointment>(
    firestore.collection("appointments"),
    setId = { med, id -> med.copy(id = id) }
) {
    override fun getModelClass() = Appointment::class.java
    override fun getId(item: Appointment) = item.id
}
