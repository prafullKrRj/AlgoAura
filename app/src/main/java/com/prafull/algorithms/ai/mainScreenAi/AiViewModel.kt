package com.prafull.algorithms.ai.mainScreenAi

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
import com.prafull.algorithms.ai.ApiKey
import com.prafull.algorithms.ai.ChatMessage
import com.prafull.algorithms.ai.ChatUiState
import com.prafull.algorithms.ai.Participant
import com.prafull.algorithms.commons.utils.Const
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class AiViewModel(
    apiKey: ApiKey,
) : ViewModel(), KoinComponent {

    private var generativeModel = GenerativeModel(modelName = "gemini-1.5-flash",
        apiKey = apiKey.apiKey,
        systemInstruction = content {
            text(Const.SYSTEM_PROMPT)
        })

    private val chat = generativeModel.startChat(
        history = listOf()
    )
    private val _uiState: MutableStateFlow<ChatUiState> =
        MutableStateFlow(ChatUiState(chat.history.map { content ->
            ChatMessage(
                text = content.parts.first().asTextOrNull() ?: "",
                participant = if (content.role == "user") Participant.USER else Participant.MODEL,
                isPending = false
            )
        }))
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    init {

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