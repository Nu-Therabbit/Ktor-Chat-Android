package com.example.ktorchat.data.remote

import com.example.ktorchat.domain.model.Message

interface MessageService {
    suspend fun getAllMessage(): List<Message>

    companion object {
        const val BASE_URL = "http://192.168.0.76:8082"
    }

    sealed class Endpoints(val url: String) {
        object GetAllMessage : Endpoints("$BASE_URL/messages")
    }
}