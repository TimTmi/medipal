package com.example.medipal.domain.service

import com.example.medipal.domain.model.Account
import com.example.medipal.domain.model.Profile

interface AccountService {
    suspend fun signIn(email: String, password: String): Account?
    suspend fun signUp(email: String, password: String, profile: Profile): Account
    suspend fun signOut()
    suspend fun getCurrentAccount(): Account?
    suspend fun getProfile(profileId: String): Profile?
}
