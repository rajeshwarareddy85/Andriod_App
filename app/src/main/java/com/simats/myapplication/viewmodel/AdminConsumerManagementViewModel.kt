package com.simats.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.myapplication.model.ConsumerManagementResponse
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminConsumerManagementViewModel : ViewModel() {
    private val _consumerState = MutableStateFlow<ConsumerManagementState>(ConsumerManagementState.Loading)
    val consumerState: StateFlow<ConsumerManagementState> = _consumerState.asStateFlow()

    fun fetchConsumers() {
        viewModelScope.launch {
            _consumerState.value = ConsumerManagementState.Loading
            try {
                val response = ApiClient.api.getConsumerManagement()
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.ok) {
                        _consumerState.value = ConsumerManagementState.Success(body)
                    } else {
                        _consumerState.value = ConsumerManagementState.Error(body.error ?: "API returned error")
                    }
                } else {
                    _consumerState.value = ConsumerManagementState.Error("HTTP Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AdminConsumerVM", "Fetch error", e)
                _consumerState.value = ConsumerManagementState.Error(e.message ?: "Unknown network error")
            }
        }
    }
}

sealed class ConsumerManagementState {
    object Loading : ConsumerManagementState()
    data class Success(val data: ConsumerManagementResponse) : ConsumerManagementState()
    data class Error(val message: String) : ConsumerManagementState()
}
