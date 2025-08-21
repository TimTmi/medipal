package com.example.medipal.domain.model

enum class Sender {
    USER,
    BOT
}

data class ChatMessage(
    val id: String = "",
    val sender: Sender = Sender.USER,
    val content: String = "",
    val timestamp: Long = 0
)