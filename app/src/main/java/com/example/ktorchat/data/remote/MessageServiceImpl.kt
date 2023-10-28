package com.example.ktorchat.data.remote

import com.example.ktorchat.data.remote.dto.MessageDto
import com.example.ktorchat.domain.model.Message
import io.ktor.client.*
import io.ktor.client.request.*

class MessageServiceImpl(private val client: HttpClient) : MessageService {
    override suspend fun getAllMessage(): List<Message> {
        return try {
            client.get<List<MessageDto>>(MessageService.Endpoints.GetAllMessage.url).map {
                it.toMessage()
            }
        } catch (e: Exception) {
            emptyList<Message>()
        }
    }
}