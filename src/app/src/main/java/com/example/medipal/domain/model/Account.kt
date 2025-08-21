package com.example.medipal.domain.model

enum class AccountType {
    CUSTOMER,
    CARETAKER
}

data class Account (
    val id: String = "",
    val accountType: AccountType = AccountType.CUSTOMER,
    val profileId: String = ""
)