package com.simats.PowerPulse.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.PowerPulse.model.AppMessage
import com.simats.PowerPulse.network.ApiClient
import com.simats.PowerPulse.model.AIChatRequest
import kotlinx.coroutines.launch

class AIViewModel : ViewModel() {
    var consumerNo by mutableStateOf("CN1001")
    
    var messages by mutableStateOf<List<AppMessage>>(listOf(
        AppMessage(
            text = "Hello! I'm your PowerPulse AI assistant. How can I help you today with your electricity usage or recharge plans?",
            isUser = false
        )
    ))
        private set

    var inputText by mutableStateOf("")
    var isTyping by mutableStateOf(false)
        private set

    fun onInputTextChanged(newValue: String) {
        inputText = newValue
    }

    fun sendMessage() {
        if (inputText.isBlank()) return
        
        val userPrompt = inputText
        val userMsg = AppMessage(text = userPrompt, isUser = true)
        
        // Build history for backend before updating local list
        val history = messages.filter { it.text != null }.map {
            com.simats.PowerPulse.model.AIChatMessage(
                role = if (it.isUser) "user" else "assistant",
                content = it.text
            )
        }

        messages = messages + userMsg
        inputText = ""
        isTyping = true

        viewModelScope.launch {
            try {
                val response = ApiClient.api.chatWithAI(
                    com.simats.PowerPulse.model.AIChatRequest(
                        message = userPrompt, 
                        consumerNo = consumerNo,
                        history = history
                    )
                )
                if (response.isSuccessful) {
                    val aiResponse = response.body()?.response ?: "Sorry, I couldn't process that."
                    messages = messages + AppMessage(text = aiResponse, isUser = false)
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    val displayMsg = try {
                        // Backend returns error in "error" field when Using fail()
                        val json = com.google.gson.JsonParser.parseString(errorBody).asJsonObject
                        json.get("error")?.asString ?: "AI Assistant is currently unavailable."
                    } catch (e: Exception) {
                        "AI Assistant is currently unavailable."
                    }
                    messages = messages + AppMessage(text = displayMsg, isUser = false)
                }
            } catch (e: Exception) {
                messages = messages + AppMessage(text = "Network error. Please try again.", isUser = false)
            } finally {
                isTyping = false
            }
        }
    }
}
