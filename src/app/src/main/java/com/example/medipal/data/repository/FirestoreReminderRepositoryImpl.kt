package com.example.medipal.data.repository

import com.example.medipal.domain.model.Reminder
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreReminderRepositoryImpl(
    firestore: FirebaseFirestore
) : FirestoreRepositoryImpl<Reminder>(
    firestore.collection("reminders"),
    setId = { med, id -> med.copy(id = id) }
) {
    override fun getModelClass() = Reminder::class.java
    override fun getId(item: Reminder) = item.id
}
