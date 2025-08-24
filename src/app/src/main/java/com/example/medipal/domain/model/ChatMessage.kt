package com.example.medipal.domain.model

import java.util.UUID

@Suppress("unused")
enum class Sender {
    USER,
    BOT
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    val sender: Sender = Sender.USER,
    val content: String = "",
    val timestamp: Long = 0
) {
    // No-argument constructor for Firestore
    constructor() : this(
        id = UUID.randomUUID().toString(),
        sender = Sender.USER,
        content = "",
        timestamp = 0
    )
}