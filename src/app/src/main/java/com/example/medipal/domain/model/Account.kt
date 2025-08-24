package com.example.medipal.domain.model

import java.util.UUID

@Suppress("unused")
enum class AccountType {
    CUSTOMER,
    CARETAKER
}

data class Account (
    val id: String = UUID.randomUUID().toString(),
    val accountType: AccountType = AccountType.CUSTOMER,
    val profileId: String = ""
)