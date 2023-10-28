package com.example.ktorchat.data.remote

import com.example.ktorchat.data.remote.dto.MessageDto
import com.example.ktorchat.domain.model.Message
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class MessageServiceImpl(private val client: HttpClient) : MessageService {
    override suspend fun getAllMessage(): List<Message> {
        return try {
            client.get(MessageService.Endpoints.GetAllMessage.url).body<List<MessageDto>>().map {
                it.toMessage()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
