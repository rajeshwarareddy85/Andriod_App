package com.simats.PowerPulse.model

import com.google.gson.annotations.SerializedName

// ---------- Common ----------
data class BasicOkResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null,

    @SerializedName("boardId")
    val boardId: String? = null
)

data class HealthResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("status")
    val status: String? = null,

    @SerializedName("error")
    val error: String? = null
)

// ---------- USER ----------
data class UserRegisterReq(
    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("mandal")
    val mandal: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)

data class UserLoginReq(
    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

// ---------- ADMIN ----------
data class AdminRegisterReq(
    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("orgName")
    val orgName: String,

    @SerializedName("boardId")
    val boardId: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)

data class AdminLoginReq(
    @SerializedName("orgName")
    val orgName: String,

    @SerializedName("boardId")
    val boardId: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)

// ---------- LOGIN RESPONSE ----------
data class LoginResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("accessToken")
    val accessToken: String? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null,

    // Fields returned by user login
    @SerializedName("email")
    val email: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("consumerNo")
    val consumerNo: String? = null,

    // Fields returned by admin login
    @SerializedName("role")
    val role: String? = null,

    // Keep these for backward compatibility if used anywhere else as nested
    @SerializedName("user")
    val user: LoggedInUser? = null,

    @SerializedName("admin")
    val admin: LoggedInAdmin? = null
)

data class LoggedInUser(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("consumerNo")
    val consumerNo: String? = null,

    @SerializedName("mandal")
    val mandal: String? = null
)

data class LoggedInAdmin(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("orgName")
    val orgName: String? = null,

    @SerializedName("boardId")
    val boardId: String? = null
)

// ---------- USER PROFILE ----------
data class UserProfileResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("user")
    val user: UserProfile? = null,

    @SerializedName("error")
    val error: String? = null
)

data class UserProfile(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("consumerNo")
    val consumerNo: String? = null,

    @SerializedName("mandal")
    val mandal: String? = null
)

data class UpdateUserProfileRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("fullName")
    val fullName: String,

    @SerializedName("mandal")
    val mandal: String
)

// ---------- FORGOT PASSWORD ----------
data class ForgotPasswordRequest(
    @SerializedName("email")
    val email: String
)

data class VerifyResetCodeRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("otp")
    val otp: String
)

// Alias for compatibility if needed elsewhere
typealias VerifyOtpRequest = VerifyResetCodeRequest

data class ResetPasswordRequest(
    @SerializedName("email")
    val email: String,

    @SerializedName("otp")
    val otp: String,

    @SerializedName("newPassword")
    val newPassword: String,

    @SerializedName("confirmPassword")
    val confirmPassword: String
)

// ---------- DAILY USAGE ----------
data class SaveDailyUsageRequest(
    @SerializedName("consumerNo")
    val consumerNo: String,

    @SerializedName("usageDate")
    val usageDate: String,

    @SerializedName("usageKwh")
    val usageKwh: Double
)

data class SaveDailyUsageResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null
)

data class DailyUsageHistoryResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("history")
    val history: List<DailyUsageItem> = emptyList(),

    @SerializedName("error")
    val error: String? = null
)

data class DailyUsageItem(
    @SerializedName("usageDate")
    val usageDate: String? = null,

    @SerializedName("usageKwh")
    val usageKwh: Double? = null
)

data class LatestDailyUsageResponse(
    @SerializedName("ok")
    val ok: Boolean = false,

    @SerializedName("found")
    val found: Boolean = false,

    @SerializedName("usageDate")
    val usageDate: String? = null,

    @SerializedName("usageKwh")
    val usageKwh: Double? = null,

    @SerializedName("isYesterday")
    val isYesterday: Boolean = false,

    @SerializedName("error")
    val error: String? = null
)

data class DashboardSummaryResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("consumerNo") val consumerNo: String?,
    @SerializedName("thisMonthTotalKwh") val thisMonthTotalKwh: Double?,
    @SerializedName("todayUsageKwh") val todayUsageKwh: Double?,
    @SerializedName("todayCost") val todayCost: Double?,
    @SerializedName("remainingDays") val remainingDays: Int?,
)

data class WeeklyTrendResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("consumerNo") val consumerNo: String?,
    @SerializedName("weeklyUsage") val weeklyUsage: List<DailyTrendItem>?,
    @SerializedName("error") val error: String?
)

data class DailyTrendItem(
    @SerializedName("date") val date: String?,
    @SerializedName("day") val day: String?,
    @SerializedName("usage") val usage: Double?
)

// ---------- FORECAST / PREDICTION ----------
data class PredictNext30Response(
    @SerializedName("ok")
    val ok: Boolean,

    @SerializedName("predictionSummary")
    val predictionSummary: PredictionSummary? = null,

    @SerializedName("predictions")
    val predictions: PredictionData? = null,

    @SerializedName("weeklyUsage")
    val weeklyUsage: List<WeeklyUsageItem>? = null,

    @SerializedName("graphData")
    val graphData: List<GraphDataItem>? = null,

    @SerializedName("message")
    val message: String? = null,

    @SerializedName("error")
    val error: String? = null
)

data class GraphDataItem(
    @SerializedName("day")
    val day: Int? = null,

    @SerializedName("label")
    val label: String? = null,

    @SerializedName("usage")
    val usage: Double? = null,

    @SerializedName("units")
    val units: Double? = null,

    @SerializedName("kwh")
    val kwh: Double? = null
)

data class PredictionSummary(
    @SerializedName("nextMonthUsageKwh")
    val nextMonthUsageKwh: Double? = null,

    @SerializedName("nextMonthUsageUnits")
    val nextMonthUsageUnits: Double? = null,

    @SerializedName("averageDailyUnits")
    val averageDailyUnits: Double? = null,

    @SerializedName("averageDailyKwh")
    val averageDailyKwh: Double? = null
)

data class PredictionData(
    @SerializedName("next_30_days")
    val next30Days: List<Double>? = null
)

data class WeeklyUsageItem(
    @SerializedName("week")
    val week: String? = null,

    @SerializedName("kwh")
    val kwh: Double? = null,

    @SerializedName("units")
    val units: Double? = null
)

// ---------- NEW DYNAMIC UPDATES ----------
data class UnitCostResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("oneUnitCost") val oneUnitCost: Double?,
    @SerializedName("currency") val currency: String?
)

data class CurrentMonthDailyTrendResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("consumerNo") val consumerNo: String?,
    @SerializedName("dailyTrend") val dailyTrend: List<DailyTrendDayItem>?
)

data class DailyTrendDayItem(
    @SerializedName("date") val date: String?,
    @SerializedName("day") val day: Int?,
    @SerializedName("usage") val usage: Double?
)

data class CurrentMonthWeeklyTrendResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("consumerNo") val consumerNo: String?,
    @SerializedName("weeklyTrend") val weeklyTrend: List<WeeklyTrendBucketItem>?
)

data class WeeklyTrendBucketItem(
    @SerializedName("weekNo") val weekNo: Int?,
    @SerializedName("startDay") val startDay: Int?,
    @SerializedName("endDay") val endDay: Int?,
    @SerializedName("usage") val usage: Double?
)

// ---------- PHASE 2 MODELS ----------
data class ActivePlanSummaryResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("active") val active: Boolean,
    @SerializedName("plan") val plan: ActivePlanDetails?
)

data class ActivePlanDetails(
    @SerializedName("planName") val planName: String?,
    @SerializedName("totalUnits") val totalUnits: Double?,
    @SerializedName("usedUnits") val usedUnits: Double?,
    @SerializedName("remainingUnits") val remainingUnits: Double?,
    @SerializedName("rechargeDate") val rechargeDate: String?,
    @SerializedName("expiryDate") val expiryDate: String?,
    @SerializedName("status") val status: String?
)

data class RechargePlanRequest(
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("planId") val planId: Int,
    @SerializedName("planName") val planName: String,
    @SerializedName("planPrice") val planPrice: Double,
    @SerializedName("planUnits") val planUnits: Double,
    @SerializedName("validityDays") val validityDays: Int = 30
)

data class AlertResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("alerts") val alerts: List<AlertItem>?
)

data class AlertItem(
    @SerializedName("title") val title: String?,
    @SerializedName("message") val message: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("date") val date: String?
)

data class AIChatMessage(
    @SerializedName("role") val role: String,
    @SerializedName("content") val content: String
)

data class AIChatRequest(
    @SerializedName("message") val message: String,
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("history") val history: List<AIChatMessage>? = null
)

data class AIChatResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("response") val response: String?
)

// ---------- RECHARGE FLOW ----------
data class RechargePlan(
    @SerializedName("id") val id: Int,
    @SerializedName("plan_name") val planName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("validity_days") val validityDays: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("units") val units: Double,
    @SerializedName("rate_per_unit") val ratePerUnit: Double? = null,
    @SerializedName("tag") val tag: String? = null,
    @SerializedName("is_recommended") val isRecommended: Boolean = false,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("display_order") val displayOrder: Int = 0,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)

data class RechargePlansResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("plans") val plans: List<RechargePlan>? = null,
    @SerializedName("error") val error: String? = null
)

// ---------- USAGE PREDICT PLANS ----------
data class UsagePredictPlan(
    @SerializedName("id") val id: Int,
    @SerializedName("plan_name") val planName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("validity_days") val validityDays: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("units") val units: Double,
    @SerializedName("rate_per_unit") val ratePerUnit: Double? = null,
    @SerializedName("tag") val tag: String? = null,
    @SerializedName("is_recommended") val isRecommended: Boolean = false,
    @SerializedName("is_active") val isActive: Boolean = true,
    @SerializedName("display_order") val displayOrder: Int = 0,
    @SerializedName("created_at") val createdAt: String? = null,
    @SerializedName("updated_at") val updatedAt: String? = null
)

data class UsagePredictPlansResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("plans") val plans: List<UsagePredictPlan>? = null,
    @SerializedName("error") val error: String? = null
)

// ---------- RECHARGE ORDERS ----------
data class CreateOrderRequest(
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("planId") val planId: Int,
    @SerializedName("planNameSnapshot") val planNameSnapshot: String,
    @SerializedName("unitsSnapshot") val unitsSnapshot: Double,
    @SerializedName("validityDaysSnapshot") val validityDaysSnapshot: Int,
    @SerializedName("baseAmount") val baseAmount: Double,
    @SerializedName("taxAmount") val taxAmount: Double,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("paymentMethod") val paymentMethod: String
)

data class CreateOrderResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("orderId") val orderId: Int? = null,
    @SerializedName("orderRef") val orderRef: String? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)

data class UpdateOrderStatusRequest(
    @SerializedName("orderId") val orderId: Int? = null,
    @SerializedName("orderRef") val orderRef: String? = null,
    @SerializedName("paymentStatus") val paymentStatus: String,
    @SerializedName("transactionRef") val transactionRef: String? = null
)

data class RechargeOrderDetails(
    @SerializedName("id") val id: Int,
    @SerializedName("orderRef") val orderRef: String,
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("planId") val planId: Int?,
    @SerializedName("planNameSnapshot") val planNameSnapshot: String?,
    @SerializedName("unitsSnapshot") val unitsSnapshot: Double?,
    @SerializedName("validityDaysSnapshot") val validityDaysSnapshot: Int?,
    @SerializedName("baseAmount") val baseAmount: Double,
    @SerializedName("taxAmount") val taxAmount: Double,
    @SerializedName("totalAmount") val totalAmount: Double,
    @SerializedName("paymentMethod") val paymentMethod: String,
    @SerializedName("paymentStatus") val paymentStatus: String,
    @SerializedName("transactionRef") val transactionRef: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("updatedAt") val updatedAt: String?
)

data class RechargeOrderResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("order") val order: RechargeOrderDetails? = null,
    @SerializedName("error") val error: String? = null
)

// ---------- ADMIN RECHARGE PLAN MANAGEMENT ----------
data class AdminCreatePlanRequest(
    @SerializedName("planName") val planName: String,
    @SerializedName("description") val description: String?,
    @SerializedName("units") val units: Double,
    @SerializedName("validityDays") val validityDays: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("tag") val tag: String?,
    @SerializedName("isRecommended") val isRecommended: Boolean,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("displayOrder") val displayOrder: Int
)

data class AdminUpdatePlanRequest(
    @SerializedName("planName") val planName: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("units") val units: Double?,
    @SerializedName("validityDays") val validityDays: Int?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("tag") val tag: String?,
    @SerializedName("isRecommended") val isRecommended: Boolean?,
    @SerializedName("isActive") val isActive: Boolean?,
    @SerializedName("displayOrder") val displayOrder: Int?
)

data class AdminTogglePlanStatusRequest(
    @SerializedName("isActive") val isActive: Boolean
)

data class AdminPlanResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("plan") val plan: RechargePlan? = null,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)

// ---------- ADMIN DASHBOARD OVERVIEW ----------
data class AdminOverviewResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("currentMonthTotalDemand") val currentMonthTotalDemand: Double = 0.0,
    @SerializedName("totalConsumers") val totalConsumers: Int = 0,
    @SerializedName("avgUnitsPerUser") val avgUnitsPerUser: Double = 0.0,
    @SerializedName("predictedNextMonthDemand") val predictedNextMonthDemand: Double = 0.0,
    @SerializedName("percentageChange") val percentageChange: Double = 0.0,
    @SerializedName("currentMonth") val currentMonth: String = "",
    @SerializedName("error") val error: String? = null
)

// ---------- ADMIN MANSAL ANALYSIS ----------
data class MandalAnalysisResponse(
    @SerializedName("ok") val ok: Boolean = false,
    @SerializedName("totalMonthlyConsumption") val totalMonthlyConsumption: Double = 0.0,
    @SerializedName("mandals") val mandals: List<MandalData> = emptyList(),
    @SerializedName("error") val error: String? = null
)

data class MandalData(
    @SerializedName("mandalName") val mandalName: String,
    @SerializedName("monthlyUsage") val monthlyUsage: Double,
    @SerializedName("currentLoadPercent") val currentLoadPercent: Int,
    @SerializedName("status") val status: String
)

data class ConsumerManagementResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("consumers") val consumers: List<ConsumerData>,
    @SerializedName("error") val error: String? = null
)

data class DailyUsagePoint(
    @SerializedName("date") val date: String,
    @SerializedName("usage") val usage: Double
)

data class ConsumerDetailsData(
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("mandal") val mandal: String,
    @SerializedName("remainingDays") val remainingDays: Int,
    @SerializedName("dailyUsage") val dailyUsage: List<DailyUsagePoint>
)

data class ConsumerDetailsResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("consumer") val consumer: ConsumerDetailsData?,
    @SerializedName("error") val error: String? = null
)

data class GenericResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("message") val message: String? = null,
    @SerializedName("error") val error: String? = null
)

data class ConsumerData(
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("mandal") val mandal: String,
    @SerializedName("avgUnits30D") val avgUnits30D: Double,
    @SerializedName("predictedNextMonth") val predictedNextMonth: Double,
    @SerializedName("status") val status: String
)

data class ChatMessage(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("senderRole") val senderRole: String,
    @SerializedName("message") val message: String,
    @SerializedName("createdAt") val createdAt: String = "",
    @SerializedName("isRead") val isRead: Boolean = false
)

data class ChatHistoryResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("history") val history: List<ChatMessage>?,
    @SerializedName("error") val error: String? = null
)

data class AdminSendMessageRequest(
    @SerializedName("consumerNo") val consumerNo: String,
    @SerializedName("message") val message: String
)

data class AdminProfileData(
    @SerializedName("id") val id: Int,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("orgName") val orgName: String,
    @SerializedName("boardId") val boardId: String,
    @SerializedName("createdAt") val createdAt: String?
)

data class AdminProfileResponse(
    @SerializedName("ok") val ok: Boolean,
    @SerializedName("admin") val admin: AdminProfileData?,
    @SerializedName("error") val error: String? = null
)
