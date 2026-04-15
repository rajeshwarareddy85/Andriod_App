package com.simats.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.myapplication.model.RechargePlan
import com.simats.myapplication.model.UsagePredictPlan
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RechargeViewModel : ViewModel() {

    private val _plans = MutableStateFlow<List<RechargePlan>>(emptyList())
    val plans: StateFlow<List<RechargePlan>> = _plans

    private val _usagePredictPlans = MutableStateFlow<List<UsagePredictPlan>>(emptyList())
    val usagePredictPlans: StateFlow<List<UsagePredictPlan>> = _usagePredictPlans

    private val _selectedTab = MutableStateFlow("RECOMMENDED") // "RECOMMENDED" or "USAGE_PREDICT"
    val selectedTab: StateFlow<String> = _selectedTab

    // We can map UsagePredictPlan to RechargePlan since the UI uses RechargePlan for selectedPlan mostly
    private val _selectedPlan = MutableStateFlow<RechargePlan?>(null)
    val selectedPlan: StateFlow<RechargePlan?> = _selectedPlan

    private val _selectedPaymentMethod = MutableStateFlow("UPI")
    val selectedPaymentMethod: StateFlow<String> = _selectedPaymentMethod

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchPlans() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = ApiClient.api.getRechargePlans()
                if (response.isSuccessful && response.body()?.ok == true) {
                    _plans.value = response.body()?.plans ?: emptyList()
                } else {
                    _error.value = response.body()?.error ?: "Failed to load predicted plans"
                }
                
                val usageResponse = ApiClient.api.getUsagePredictPlans()
                if (usageResponse.isSuccessful && usageResponse.body()?.ok == true) {
                    _usagePredictPlans.value = usageResponse.body()?.plans ?: emptyList()
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "An unexpected error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun selectPlan(plan: RechargePlan) {
        _selectedPlan.value = plan
    }

    fun selectUsagePredictPlan(plan: UsagePredictPlan) {
        // Map UsagePredictPlan to expected RechargePlan format for down-stream Payment logic
        _selectedPlan.value = RechargePlan(
            id = plan.id,
            planName = plan.planName,
            description = plan.description,
            validityDays = plan.validityDays,
            amount = plan.amount,
            units = plan.units,
            ratePerUnit = plan.ratePerUnit,
            tag = plan.tag,
            isRecommended = plan.isRecommended,
            isActive = plan.isActive,
            displayOrder = plan.displayOrder,
            createdAt = plan.createdAt,
            updatedAt = plan.updatedAt
        )
    }

    fun selectPaymentMethod(method: String) {
        _selectedPaymentMethod.value = method
    }
}
