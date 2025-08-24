package com.example.medipal.data.repository

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreReminderRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileId: String
) : FirestoreRepositoryImpl<Reminder>(
    firestore.collection("profiles").document(profileId).collection("reminders"),
    Reminder::class.java,
    setId = { reminder, id -> reminder.copy(id = id) }
), ReminderRepository {
    override fun getId(item: Reminder) = item.id
}
