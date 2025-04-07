package com.example.websocket_chat

import io.ktor.http.URLProtocol
import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.plugins.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.*

/**
 * Manager class for handling WebSocket connections using Ktor
 */
internal class WebSocketManager {
    companion object {
        private const val TAG = "WebSocketManager"
        private const val WEBSOCKET_URL = "wss://echo.websocket.org"
        private const val SPECIAL_MESSAGE = "This is a special message from the server!"
    }

    private val client = HttpClient(CIO) {
        install(WebSockets)
        install(Logging) {
            level = LogLevel.INFO
        }
    }

    private var webSocketSession: DefaultClientWebSocketSession? = null
    private var messageListener: MessageListener? = null
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var isConnected = false

    /**
     * Interface for receiving message callbacks
     */
    interface MessageListener {
        fun onMessageReceived(message: String)
        fun onConnectionStatusChanged(connected: Boolean)
    }

    /**
     * Sets the message listener
     */
    fun setMessageListener(listener: MessageListener) {
        this.messageListener = listener
    }

    /**
     * Connects to the WebSocket server
     */
    fun connect() {
        coroutineScope.launch {
            try {
                client.wss(WEBSOCKET_URL){
                    webSocketSession = this
                    isConnected = true

                    withContext(Dispatchers.Main) {
                        messageListener?.onConnectionStatusChanged(true)
                    }

                    try {
                        while (true){
                            val othersMessage = incoming.receive() as? Frame.Text
                            val text = othersMessage?.readText()

                            val finalMessage = if (matchHexPattern(text.orEmpty())) {
                                SPECIAL_MESSAGE
                            } else {
                                text
                            }

                            withContext(Dispatchers.Main) {
                                messageListener?.onMessageReceived(finalMessage.orEmpty())
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error while receiving: ${e.message}")
                    } finally {
                        isConnected = false
                        withContext(Dispatchers.Main) {
                            messageListener?.onConnectionStatusChanged(false)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "WebSocket connection error: ${e.message}")
                isConnected = false
                withContext(Dispatchers.Main) {
                    messageListener?.onConnectionStatusChanged(false)
                }
            }
        }
    }

    private fun matchHexPattern(input: String): Boolean {
        val regex = Regex("""\d+\s*=\s*0x[a-fA-F0-9]+""")
        return regex.matches(input)
    }

    /**
     * Sends a message through the WebSocket connection
     */
    fun sendMessage(message: String) {
        if (!isConnected) {
            Log.w(TAG, "Cannot send message: WebSocket is not connected")
            return
        }

        coroutineScope.launch {
            try {
                webSocketSession?.send(Frame.Text(message))
            } catch (e: Exception) {
                Log.e(TAG, "Error sending message: ${e.message}")
            }
        }
    }

    /**
     * Closes the WebSocket connection
     */
    fun disconnect() {
        coroutineScope.launch {
            try {
                webSocketSession?.close()
            } catch (e: Exception) {
                Log.e(TAG, "Error closing WebSocket: ${e.message}")
            } finally {
                isConnected = false
                webSocketSession = null
            }
        }
        coroutineScope.cancel()
    }
}