package com.simats.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.myapplication.model.MandalAnalysisResponse
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminSectorAnalysisViewModel : ViewModel() {
    private val _mandalState = MutableStateFlow<MandalAnalysisState>(MandalAnalysisState.Loading)
    val mandalState: StateFlow<MandalAnalysisState> = _mandalState.asStateFlow()

    fun fetchMandalAnalysis() {
        viewModelScope.launch {
            _mandalState.value = MandalAnalysisState.Loading
            try {
                val response = ApiClient.api.getMandalAnalysis()
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.ok) {
                        _mandalState.value = MandalAnalysisState.Success(body)
                    } else {
                        _mandalState.value = MandalAnalysisState.Error(body.error ?: "API returned error")
                    }
                } else {
                    _mandalState.value = MandalAnalysisState.Error("HTTP Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AdminSectorAnalysisVM", "Fetch error", e)
                _mandalState.value = MandalAnalysisState.Error(e.message ?: "Unknown network error")
            }
        }
    }
}

sealed class MandalAnalysisState {
    object Loading : MandalAnalysisState()
    data class Success(val data: MandalAnalysisResponse) : MandalAnalysisState()
    data class Error(val message: String) : MandalAnalysisState()
}
