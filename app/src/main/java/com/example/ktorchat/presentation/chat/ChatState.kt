package com.example.ktorchat.presentation.chat

import com.example.ktorchat.domain.model.Message

data class ChatState(val messages: List<Message> = emptyList(), val isLoading: Boolean = false)
