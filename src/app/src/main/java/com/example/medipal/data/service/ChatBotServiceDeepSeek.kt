package com.example.medipal.data.service

import com.example.medipal.domain.model.ChatMessage
import com.example.medipal.domain.model.Sender
import com.example.medipal.domain.service.ChatbotService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.UUID

class ChatBotServiceDeepSeek(
    private val accessToken: String
) : ChatbotService {

    private val client = OkHttpClient()
    private val _conversation = MutableStateFlow<List<ChatMessage>>(emptyList())

    override fun getConversation(): Flow<List<ChatMessage>> = _conversation

    override suspend fun clearConversation() {
        _conversation.value = emptyList()
    }

    override suspend fun sendMessage(message: String): ChatMessage = withContext(Dispatchers.IO) {
        // Add user message to conversation
        val userMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = Sender.USER,
            content = message,
            timestamp = System.currentTimeMillis()
        )
        _conversation.value = _conversation.value + userMessage

        // Call DeepSeek API
        val botReply = try {
            val url = "https://api.deepseek.com/v1/chat/completions"

            val json = JSONObject().apply {
                put("model", "deepseek-chat")
                put("messages", listOf(mapOf("role" to "user", "content" to message)))
            }

            val body = json.toString().toRequestBody("application/json".toMediaType())

            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $accessToken")
                .post(body)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    "Error: ${response.code}"
                } else {
                    val responseBody = response.body?.string() ?: ""
                    val jsonResponse = JSONObject(responseBody)
                    // adjust this according to DeepSeek's actual response
                    jsonResponse.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "Something went wrong: ${e.message}"
        }

        // Add bot reply to conversation
        val botMessage = ChatMessage(
            id = UUID.randomUUID().toString(),
            sender = Sender.BOT,
            content = botReply,
            timestamp = System.currentTimeMillis()
        )
        _conversation.value = _conversation.value + botMessage

        return@withContext botMessage
    }
}