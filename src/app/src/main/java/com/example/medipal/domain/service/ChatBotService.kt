package com.example.medipal.domain.service

import com.example.medipal.domain.model.ChatMessage
import kotlinx.coroutines.flow.Flow

interface ChatbotService {
    suspend fun sendMessage(message: String): ChatMessage
    fun getConversation(): Flow<List<ChatMessage>>
    suspend fun clearConversation()
}