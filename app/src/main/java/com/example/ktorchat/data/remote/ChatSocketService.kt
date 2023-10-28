package com.example.ktorchat.data.remote

import com.example.ktorchat.domain.model.Message
import com.example.ktorchat.util.Resource
import kotlinx.coroutines.flow.Flow

interface ChatSocketService {
    suspend fun initSession(username: String): Resource<Unit>
    suspend fun sendMessage(message: String)
    fun observeMessage(): Flow<Message>
    suspend fun closeSession()

    companion object {
        const val BASE_URL = "ws://192.168.0.76:8082"
    }

    sealed class Endpoints(val url: String) {
        object ChatSocket : Endpoints("$BASE_URL/chat-socket")
    }
}