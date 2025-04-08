package kz.diasjakupov.websocket_chat

internal data class MessageItem(
    val id: Long,
    val message: String,
    val isFromUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
