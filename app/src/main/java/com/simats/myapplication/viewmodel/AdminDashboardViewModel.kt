package com.simats.myapplication.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.myapplication.model.AdminOverviewResponse
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminDashboardViewModel : ViewModel() {
    private val _overviewState = MutableStateFlow<AdminOverviewState>(AdminOverviewState.Loading)
    val overviewState: StateFlow<AdminOverviewState> = _overviewState.asStateFlow()

    fun fetchDashboardOverview() {
        viewModelScope.launch {
            _overviewState.value = AdminOverviewState.Loading
            try {
                val response = ApiClient.api.getAdminDashboardOverview()
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.ok) {
                        _overviewState.value = AdminOverviewState.Success(body)
                    } else {
                        _overviewState.value = AdminOverviewState.Error(body.error ?: "API returned error")
                    }
                } else {
                    _overviewState.value = AdminOverviewState.Error("HTTP Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AdminDashboardViewModel", "Fetch error", e)
                _overviewState.value = AdminOverviewState.Error(e.message ?: "Unknown network error")
            }
        }
    }
}

sealed class AdminOverviewState {
    object Loading : AdminOverviewState()
    data class Success(val data: AdminOverviewResponse) : AdminOverviewState()
    data class Error(val message: String) : AdminOverviewState()
}
