package com.example.medipal.data.service

import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.Profile
import com.example.medipal.domain.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class FirebaseAccountServiceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AccountService {

    private val accountsCollection = firestore.collection("accounts")
    private val profilesCollection = firestore.collection("profiles")

    override suspend fun signIn(email: String, password: String): Account? {
        val result = auth.signInWithEmailAndPassword(email, password).await()
        val user = result.user ?: return null

        val snapshot = accountsCollection.document(user.uid).get().await()
        return snapshot.toObject(Account::class.java)
    }

    override suspend fun signUp(email: String, password: String, profile: Profile): Account {
        val result = auth.createUserWithEmailAndPassword(email, password).await()
        val user = result.user ?: throw IllegalStateException("User creation failed")

        // 1. Create Profile
        val newProfile = profile.copy(id = profilesCollection.document().id)
        profilesCollection.document(newProfile.id).set(newProfile).await()

        // 2. Create Account linked to Profile
        val account = Account(
            id = user.uid,
            email = email,
            profileId = newProfile.id
        )
        accountsCollection.document(account.id).set(account).await()

        return account
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun getCurrentAccount(): Account? {
        val user = auth.currentUser ?: return null
        val snapshot = accountsCollection.document(user.uid).get().await()
        return snapshot.toObject(Account::class.java)
    }

    override suspend fun getProfile(profileId: String): Profile? {
        val snapshot = profilesCollection.document(profileId).get().await()
        return snapshot.toObject(Profile::class.java)
    }
}