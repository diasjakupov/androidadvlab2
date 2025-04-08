package kz.diasjakupov.websocket_chat


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kz.diasjakupov.websocket_chat.databinding.ItemMessageBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adapter for the chat RecyclerView using View Binding
 */
internal class ChatAdapter : RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    private val messages = mutableListOf<MessageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    /**
     * Adds a new message to the adapter and notifies the UI
     */
    fun addMessage(message: MessageItem) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    /**
     * ViewHolder for chat messages using View Binding
     */
    inner class MessageViewHolder(private val binding: ItemMessageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(messageItem: MessageItem) {
            if (messageItem.isFromUser) {
                binding.textViewUserMessage.visibility = View.VISIBLE
                binding.textViewUserMessage.text = messageItem.message
                binding.textViewServerMessage.visibility = View.GONE

                binding.textViewUserTimestamp.visibility = View.VISIBLE
                binding.textViewUserTimestamp.text = formatTimestamp(messageItem.timestamp)
                binding.textViewServerTimestamp.visibility = View.GONE
            } else {
                binding.textViewServerMessage.visibility = View.VISIBLE
                binding.textViewServerMessage.text = messageItem.message
                binding.textViewUserMessage.visibility = View.GONE

                binding.textViewServerTimestamp.visibility = View.VISIBLE
                binding.textViewServerTimestamp.text = formatTimestamp(messageItem.timestamp)
                binding.textViewUserTimestamp.visibility = View.GONE
            }
        }

        /**
         * Formats the timestamp (milliseconds) into a readable time string.
         */
        private fun formatTimestamp(timestamp: Long): String {
            val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
    }
}
