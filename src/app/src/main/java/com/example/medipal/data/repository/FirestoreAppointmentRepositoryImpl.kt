package com.example.medipal.data.repository

import com.example.medipal.domain.model.Appointment
import com.example.medipal.domain.repository.AppointmentRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreAppointmentRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileId: String
) : FirestoreRepositoryImpl<Appointment>(
    firestore.collection("profiles").document(profileId).collection("appointments"),
    Appointment::class.java,
    setId = { appointment, id -> appointment.copy(id = id) }
), AppointmentRepository {
    override fun getId(item: Appointment) = item.id
}
