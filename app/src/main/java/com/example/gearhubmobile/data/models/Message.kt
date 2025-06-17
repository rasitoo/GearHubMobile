package com.example.gearhubmobile.data.models

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
data class Message(
    val id: String,
    val chatId: String,
    val senderId: String,
    val content: String?,
    val sentAt: String?
){
    val sentAtDate: java.util.Date?
        get() = try {
            val isoFormat = java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", java.util.Locale.getDefault())
            isoFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            isoFormat.parse(sentAt)
        } catch (e: Exception) {
            null
        }
}


data class CreateMessageRequest(
    val content: String,
    val chatId: String
)

data class UpdateMessageRequest(
    val content: String
)