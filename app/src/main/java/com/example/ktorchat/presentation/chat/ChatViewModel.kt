package com.example.ktorchat.presentation.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ktorchat.data.remote.ChatSocketService
import com.example.ktorchat.data.remote.MessageService
import com.example.ktorchat.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val messageService: MessageService,
    private val chatSocketService: ChatSocketService,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _messageText = MutableStateFlow("")
    val messageText: StateFlow<String> = _messageText

    private val _state = MutableStateFlow(ChatState())
    val state: StateFlow<ChatState> = _state

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent = _toastEvent.asSharedFlow()

    init {
        getAllMessage()
        getRandomString(5)?.let { username ->
            viewModelScope.launch {
                when (val result = chatSocketService.initSession(username)) {
                    is Resource.Error -> {
                        _toastEvent.emit(result.message ?: "Error?")
                    }
                    is Resource.Success -> {
                        chatSocketService.observeMessage()
                            .onEach { message ->
                                Log.d("xxx ", "message $message")
                                val newList = state.value.messages.toMutableList().apply {
                                    add(0, message)
                                }
                                _state.value = state.value.copy(messages = newList)
                            }.launchIn(viewModelScope)
                    }
                }
            }
        }
    }

    private fun getRandomString(length: Int): String? {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        return (1..length)
            .map { allowedChars.random() }
            .joinToString("")
    }

    fun onMessageChange(message: String) {
        _messageText.value = message
    }

    fun disconnect() {
        viewModelScope.launch {
            chatSocketService.closeSession()
        }
    }

    fun getAllMessage() {
        viewModelScope.launch {
            _state.value = state.value.copy(isLoading = true)
            val result = messageService.getAllMessage()
            _state.value = state.value.copy(messages = result, isLoading = false)
        }
    }

    fun sendMessage() {
        viewModelScope.launch {
            if (messageText.value.isNotBlank()) {
                chatSocketService.sendMessage(messageText.value)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        disconnect()
    }
}