package com.example.medipal.domain.model

import java.util.UUID

@Suppress("unused")
enum class AccountType {
    CUSTOMER,
    CAREGIVER
}

data class Account(
    val id: String = "",
    val email: String = "",
    val type: AccountType = AccountType.CUSTOMER,
    val profileId: String = ""
) {
    // No-argument constructor for Firestore
    constructor() : this("", "", AccountType.CUSTOMER, "")
}

data class CaregiverAssignment(
    val caregiverId: String = "",
    val customerId: String = ""
)