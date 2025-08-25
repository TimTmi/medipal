package com.example.medipal.data.repository

import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.repository.ProfileRepository
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreProfileRepositoryImpl(
    firestore: FirebaseFirestore
) : FirestoreRepositoryImpl<Profile>(
    { firestore.collection("profiles") },
    Profile::class.java,
    setId = { profile, id -> profile.copy(id = id) }
), ProfileRepository {
    override fun getId(item: Profile) = item.id
}