package com.example.medipal.domain.repository

import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow

interface RemoteRepository<T> : Repository<T> {
    fun getCollection(): CollectionReference? = null
}