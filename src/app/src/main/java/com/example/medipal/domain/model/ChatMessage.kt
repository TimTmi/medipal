package com.example.medipal.domain.model

enum class Sender {
    USER,
    BOT
}

data class ChatMessage(
    val id: String,
    val sender: Sender,
    val content: String,
    val timestamp: Long
)