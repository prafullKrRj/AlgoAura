package com.prafull.algorithms.ai

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import com.prafull.algorithms.Routes
import com.prafull.algorithms.commons.utils.Const
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import java.util.UUID

class ChatViewModel(
    private val askAi: Routes.AskAi,
    apiKey: ApiKey
) : ViewModel(), KoinComponent {

    val language: String by mutableStateOf(askAi.language)
    var code by mutableStateOf(askAi.code)
    var programName by mutableStateOf(askAi.programName)
    var message by mutableStateOf(askAi.message)

    private var generativeModel = GenerativeModel(modelName = "gemini-1.5-flash",
        apiKey = apiKey.apiKey,
        systemInstruction = content {
            text(Const.SYSTEM_PROMPT + "Refer this following Code: " + askAi.code)
        })

    private val chat = generativeModel.startChat(
        history = listOf()
    )

    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState(chat.history.map { content ->
            // Map the initial messages
            ChatMessage(
                text = content.parts.first().asTextOrNull() ?: "",
                participant = if (content.role == "user") Participant.USER else Participant.MODEL,
                isPending = false
            )
        }))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    val isLoading by mutableStateOf(false)

    init {
        sendMessage(askAi.message)
    }

    fun sendMessage(userMessage: String) {
        // Add a pending message
        _uiState.value.addMessage(
            ChatMessage(
                text = userMessage, participant = Participant.USER, isPending = true
            )
        )

        viewModelScope.launch {
            try {
                val response = chat.sendMessage(userMessage)

                _uiState.value.replaceLastPendingMessage()

                response.text?.let { modelResponse ->
                    _uiState.value.addMessage(
                        ChatMessage(
                            text = modelResponse, participant = Participant.MODEL, isPending = false
                        )
                    )
                }
            } catch (e: Exception) {
                _uiState.value.replaceLastPendingMessage()
                _uiState.value.addMessage(
                    ChatMessage(
                        text = e.localizedMessage, participant = Participant.ERROR
                    )
                )
                Log.d("ChatViewModel", "Error: ${e.localizedMessage}")
            }
        }
    }
}

enum class Participant {
    USER, MODEL, ERROR
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Participant = Participant.USER,
    var isPending: Boolean = true
)

class ChatUiState(
    messages: List<ChatMessage> = emptyList()
) {
    private val _messages: MutableList<ChatMessage> = messages.toMutableStateList()
    val messages: List<ChatMessage> = _messages

    fun addMessage(msg: ChatMessage) {
        _messages.add(msg)
    }

    fun replaceLastPendingMessage() {
        val lastMessage = _messages.lastOrNull()
        lastMessage?.let {
            val newMessage = lastMessage.apply { isPending = false }
            _messages.removeAt(_messages.size - 1)
            _messages.add(newMessage)
        }
    }
}
