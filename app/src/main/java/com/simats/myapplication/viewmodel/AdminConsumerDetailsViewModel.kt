package com.simats.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.myapplication.model.ConsumerDetailsData
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminConsumerDetailsViewModel : ViewModel() {
    private val _consumerDetails = MutableStateFlow<ConsumerDetailsData?>(null)
    val consumerDetails = _consumerDetails.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted = _isDeleted.asStateFlow()

    fun fetchConsumerDetails(consumerNo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiClient.api.getConsumerDetails(consumerNo)
                if (response.isSuccessful && response.body()?.ok == true) {
                    _consumerDetails.value = response.body()?.consumer
                } else {
                    _error.value = response.body()?.error ?: "Failed to load details"
                }
            } catch (e: Exception) {
                Log.e("AdminConsumerDetailsVM", "Error fetching details", e)
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteConsumer(consumerNo: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiClient.api.deleteConsumer(consumerNo)
                if (response.isSuccessful && response.body()?.ok == true) {
                    _isDeleted.value = true
                } else {
                    _error.value = response.body()?.error ?: "Failed to delete consumer"
                }
            } catch (e: Exception) {
                Log.e("AdminConsumerDetailsVM", "Error deleting consumer", e)
                _error.value = "Network error: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetState() {
        _isDeleted.value = false
        _error.value = null
        _consumerDetails.value = null
    }
}
