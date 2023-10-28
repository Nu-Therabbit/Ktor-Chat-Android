package com.example.ktorchat.data.remote.dto

import com.example.ktorchat.domain.model.Message
import kotlinx.serialization.Serializable
import java.text.DateFormat
import java.util.*

@Serializable
data class MessageDto(
    val id: String,
    val text: String,
    val userName: String,
    val timestamp: Long,
) {
    fun toMessage(): Message {
        val date = Date(timestamp)
        val formattedDate = DateFormat.getDateInstance(DateFormat.DEFAULT)
            .format(date)
        return Message(
            text = text,
            username = userName,
            time = formattedDate
        )
    }
}