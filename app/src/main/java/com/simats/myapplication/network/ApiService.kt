package com.simats.PowerPulse.network

import com.simats.PowerPulse.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("health")
    suspend fun health(): Response<HealthResponse>

    @POST("auth/user/register")
    suspend fun registerUser(
        @Body request: UserRegisterReq
    ): Response<BasicOkResponse>

    @POST("auth/user/login")
    suspend fun loginUser(
        @Body request: UserLoginReq
    ): Response<LoginResponse>

    @GET("auth/user-profile")
    suspend fun getUserProfileByEmail(
        @Query("email") email: String
    ): Response<UserProfileResponse>

    @POST("auth/user-profile/update")
    suspend fun updateUserProfile(
        @Body request: UpdateUserProfileRequest
    ): Response<BasicOkResponse>

    @POST("auth/admin/register")
    suspend fun registerAdmin(
        @Body request: AdminRegisterReq
    ): Response<BasicOkResponse>

    @POST("auth/admin/login")
    suspend fun loginAdmin(
        @Body request: AdminLoginReq
    ): Response<LoginResponse>

    @GET("auth/admin/profile")
    suspend fun getAdminProfileByEmail(
        @Query("email") email: String
    ): Response<AdminProfileResponse>

    @POST("auth/forgot/request")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<BasicOkResponse>

    @POST("auth/forgot/verify")
    suspend fun verifyOtp(
        @Body request: VerifyResetCodeRequest
    ): Response<BasicOkResponse>

    @POST("auth/forgot/reset")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<BasicOkResponse>

    @GET("user/daily-usage-history")
    suspend fun getDailyUsageHistory(
        @Query("consumerNo") consumerNo: String
    ): Response<DailyUsageHistoryResponse>

    @GET("user/latest-daily-usage")
    suspend fun getLatestDailyUsage(
        @Query("consumerNo") consumerNo: String
    ): Response<LatestDailyUsageResponse>

    @GET("user/dashboard-summary")
    suspend fun getDashboardSummary(
        @Query("consumerNo") consumerNo: String
    ): Response<DashboardSummaryResponse>

    @GET("user/weekly-usage")
    suspend fun getWeeklyTrend(
        @Query("consumerNo") consumerNo: String
    ): Response<WeeklyTrendResponse>

    @POST("user/daily-usage")
    suspend fun saveDailyUsage(
        @Body request: SaveDailyUsageRequest
    ): Response<SaveDailyUsageResponse>

    @GET("predict-next-30-from-db")
    suspend fun predictNext30FromDb(
        @Query("consumerNo") consumerNo: String
    ): Response<PredictNext30Response>

    @GET("admin/unit-cost")
    suspend fun getUnitCost(): Response<UnitCostResponse>

    @GET("user/current-month-daily-trend")
    suspend fun getCurrentMonthDailyTrend(
        @Query("consumerNo") consumerNo: String
    ): Response<CurrentMonthDailyTrendResponse>

    @GET("user/current-month-weekly-trend")
    suspend fun getCurrentMonthWeeklyTrend(
        @Query("consumerNo") consumerNo: String
    ): Response<CurrentMonthWeeklyTrendResponse>

    @POST("user/recharge-plan")
    suspend fun rechargePlan(@Body request: RechargePlanRequest): Response<BasicOkResponse>

    @GET("user/active-plan-summary")
    suspend fun getActivePlanSummary(
        @Query("consumerNo") consumerNo: String
    ): Response<ActivePlanSummaryResponse>

    @GET("user/alerts")
    suspend fun getAlerts(
        @Query("consumerNo") consumerNo: String
    ): Response<AlertResponse>

    @POST("api/ai-chat")
    suspend fun chatWithAI(@Body request: AIChatRequest): Response<AIChatResponse>

    @GET("api/recharge-plans")
    suspend fun getRechargePlans(): Response<RechargePlansResponse>

    @GET("api/usage-predict-plans")
    suspend fun getUsagePredictPlans(): Response<UsagePredictPlansResponse>

    @GET("admin/dashboard-overview")
    suspend fun getAdminDashboardOverview(): Response<AdminOverviewResponse>

    @POST("auth/user/register")
    suspend fun registerConsumer(
        @Body request: UserRegisterReq
    ): Response<BasicOkResponse>

    @GET("admin/mandal-analysis")
    suspend fun getMandalAnalysis(): Response<MandalAnalysisResponse>

    @GET("admin/consumers-management")
    suspend fun getConsumerManagement(): Response<ConsumerManagementResponse>

    @GET("admin/consumer-details/{consumerNo}")
    suspend fun getConsumerDetails(@Path("consumerNo") consumerNo: String): Response<ConsumerDetailsResponse>

    @DELETE("admin/consumer/{consumerNo}")
    suspend fun deleteConsumer(@Path("consumerNo") consumerNo: String): Response<GenericResponse>

    @GET("chat/history/{consumerNo}")
    suspend fun getChatHistory(@Path("consumerNo") consumerNo: String): Response<ChatHistoryResponse>

    @POST("admin/send-message")
    suspend fun sendAdminMessage(@Body request: AdminSendMessageRequest): Response<GenericResponse>
}
