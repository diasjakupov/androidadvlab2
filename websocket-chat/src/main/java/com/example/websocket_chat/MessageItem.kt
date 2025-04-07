package com.example.websocket_chat

data class MessageItem(
    val id: Long,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
