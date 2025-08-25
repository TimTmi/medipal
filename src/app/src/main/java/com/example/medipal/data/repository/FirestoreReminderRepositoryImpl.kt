package com.example.medipal.data.repository

import com.example.medipal.domain.model.Reminder
import com.example.medipal.domain.repository.ReminderRepository
import com.example.medipal.util.ProfileRepositoryManager
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreReminderRepositoryImpl(
    firestore: FirebaseFirestore,
    private val profileRepositoryManager: ProfileRepositoryManager
) : FirestoreRepositoryImpl<Reminder>(
    {
        firestore.collection("profiles")
            .document(profileRepositoryManager
                .getCurrentProfileId())
            .collection("reminders")
    },
    Reminder::class.java,
    setId = { reminder, id -> reminder.copy(id = id) }
), ReminderRepository {
    override fun getId(item: Reminder) = item.id
}
