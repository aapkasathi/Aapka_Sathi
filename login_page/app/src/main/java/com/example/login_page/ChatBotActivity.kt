package com.example.login_page // change this to your package name

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.EditText
import com.example.login_page.adapter.ChatAdapter
import com.example.login_page.model.ChatMessage

class ChatBotActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageInput: EditText
    private lateinit var sendBtn: Button
    private lateinit var chatAdapter: ChatAdapter
    private val chatList = ArrayList<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_bot)

        chatRecyclerView = findViewById(R.id.chatRecyclerView)
        messageInput = findViewById(R.id.messageInput)
        sendBtn = findViewById(R.id.sendBtn)

        chatAdapter = ChatAdapter(chatList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = chatAdapter

        sendBtn.setOnClickListener {
            val message = messageInput.text.toString().trim()
            if (message.isNotEmpty()) {
                // User message
                addMessage(ChatMessage(message, true))
                // Bot reply
                val reply = getBotReply(message)
                addMessage(ChatMessage(reply, false))
                messageInput.text.clear()
                chatRecyclerView.scrollToPosition(chatList.size - 1)
            }
        }
    }

    private fun addMessage(message: ChatMessage) {
        chatList.add(message)
        chatAdapter.notifyItemInserted(chatList.size - 1)
    }

    private fun getBotReply(userMessage: String): String {
        return when {
            userMessage.contains("hello", ignoreCase = true) -> "Hi there 👋 How can I help you?"
            userMessage.contains("how are you", ignoreCase = true) -> "I'm just code 🤖 but I'm doing great!"
            userMessage.contains("bye", ignoreCase = true) -> "Goodbye! 👋 Have a nice day!"
            else -> "Hmm 🤔 I’m not sure what you mean."
        }
    }
}
