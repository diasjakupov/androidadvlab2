package com.example.websocket_chat

import android.content.Context
import android.content.Intent

class ChatBuilder {
    companion object {
        /**
         * Starts the chat activity.
         * This is the only public method of the library.
         *
         * @param context The context to start the activity from
         */
        fun start(context: Context) {
            val intent = Intent(context, ChatActivity::class.java)
            context.startActivity(intent)
        }
    }
}