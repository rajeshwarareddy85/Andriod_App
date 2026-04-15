package com.simats.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.myapplication.model.AdminSendMessageRequest
import com.simats.myapplication.model.ChatMessage
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminChatViewModel : ViewModel() {
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory = _chatHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    fun fetchChatHistory(consumerNo: String) {
        viewModelScope.launch {
            if (consumerNo.isEmpty()) return@launch
            _isLoading.value = true
            try {
                val response = ApiClient.api.getChatHistory(consumerNo)
                if (response.isSuccessful && response.body()?.ok == true) {
                    _chatHistory.value = response.body()?.history ?: emptyList()
                } else {
                    _error.value = response.body()?.error ?: "Failed to load chat history"
                }
            } catch (e: Exception) {
                Log.e("AdminChatVM", "Error fetching history", e)
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun sendMessage(consumerNo: String, messageText: String) {
        if (messageText.isBlank()) return
        viewModelScope.launch {
            try {
                val request = AdminSendMessageRequest(consumerNo, messageText)
                val response = ApiClient.api.sendAdminMessage(request)
                if (response.isSuccessful && response.body()?.ok == true) {
                    // Refresh history after sending
                    fetchChatHistory(consumerNo)
                } else {
                    _error.value = response.body()?.error ?: "Failed to send message"
                }
            } catch (e: Exception) {
                Log.e("AdminChatVM", "Error sending message", e)
                _error.value = "Network error"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
