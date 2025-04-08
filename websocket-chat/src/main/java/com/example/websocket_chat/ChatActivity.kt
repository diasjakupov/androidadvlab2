package com.example.websocket_chat

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.websocket_chat.databinding.ChatActivityBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

/**
 * Activity that provides the chat UI and functionality
 */
class ChatActivity : AppCompatActivity(), WebSocketManager.MessageListener {

    private lateinit var binding: ChatActivityBinding

    private val chatAdapter = ChatAdapter()
    private val webSocketManager = WebSocketManager()
    private val activityScope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ChatActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Chat"

        binding.recyclerViewChat.layoutManager = LinearLayoutManager(this).apply {
            stackFromEnd = true
        }
        binding.recyclerViewChat.adapter = chatAdapter

        webSocketManager.setMessageListener(this)
        webSocketManager.connect()

        binding.buttonSend.setOnClickListener {
            val message = binding.editTextMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.editTextMessage.text.clear()
            }
        }
    }

    /**
     * Generates a random long number for message IDs.
     */
    private fun generateMessageId(): Long = kotlin.random.Random.nextLong()

    private fun sendMessage(message: String) {
        val messageId = generateMessageId()
        val userMessageItem = MessageItem(messageId, message, true)
        chatAdapter.addMessage(userMessageItem)
        scrollToBottom()
        webSocketManager.sendMessage(message)
    }

    /**
     * Callback for received messages from WebSocket
     */
    override fun onMessageReceived(message: String) {
        val messageId = generateMessageId()
        val serverMessageItem = MessageItem(messageId, message, false)
        chatAdapter.addMessage(serverMessageItem)
        scrollToBottom()
    }

    /**
     * Callback for WebSocket connection status changes
     */
    override fun onConnectionStatusChanged(connected: Boolean) {
        val toastMessage = if (connected) {
            "Connected to chat server"
        } else {
            "Disconnected from chat server"
        }
        Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
    }

    /**
     * Scrolls the RecyclerView to the bottom to show the latest message
     */
    private fun scrollToBottom() {
        binding.recyclerViewChat.post {
            binding.recyclerViewChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketManager.disconnect()
        activityScope.cancel()
    }
}