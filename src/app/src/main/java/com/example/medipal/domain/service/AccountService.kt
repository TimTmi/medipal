package com.example.medipal.domain.service

import com.example.medipal.domain.model.Account

interface AccountService {
    suspend fun signIn(email: String, password: String): Account
    suspend fun signUp(email: String, password: String): Account
    suspend fun signOut()
    fun getCurrentAccount(): Account?
}