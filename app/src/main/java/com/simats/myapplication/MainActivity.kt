package com.simats.PowerPulse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simats.PowerPulse.ui.theme.PowerPulseTheme
import com.simats.PowerPulse.viewmodel.AIViewModel
import com.simats.PowerPulse.viewmodel.RechargeViewModel
import com.simats.PowerPulse.viewmodel.AdminConsumerManagementViewModel
import com.simats.PowerPulse.viewmodel.AdminConsumerDetailsViewModel
import com.simats.PowerPulse.viewmodel.AdminChatViewModel

enum class Screen {
    Splash,
    Onboarding,
    AccountSelection,
    ConsumerLogin,
    ConsumerSignUp,
    ForgotPassword,
    VerifyResetCode,
    ResetPassword,
    Dashboard,
    AdminLogin,
    AdminSignUp,
    AdminDashboard,
    AdminConsumerManagement,
    AdminSectorAnalysis,
    AdminConsumerDetails,
    RechargePlans,
    PaymentReview,
    RechargeOrderSummary,
    SelectPlanDuration,
    EnterMonthlyUsage,
    MonthlyUsageForecast,
    AIAssistant,
    Profile,
    AccountDetails,
    CardDetails,
    NetBanking,
    NotificationPreferences,
    DailyUsageHistory,
    PrivacyPolicy,
    AddConsumer,
    AdminChat,
    AdminProfile,
    AdminReports,
    Subscription
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            PowerPulseTheme {

                var currentScreen by rememberSaveable { mutableStateOf(Screen.Splash) }

                var emailForReset by rememberSaveable { mutableStateOf("") }
                var verifiedOtp by rememberSaveable { mutableStateOf("") }
                var entryPoint by rememberSaveable { mutableStateOf<Screen?>(null) }

                var loggedInUserEmail by rememberSaveable { mutableStateOf("") }
                var loggedInConsumerNo by rememberSaveable { mutableStateOf("") }
                var loggedInAdminEmail by rememberSaveable { mutableStateOf("") }
                var selectedConsumerId by rememberSaveable { mutableStateOf("") }

                val aiViewModel: AIViewModel = viewModel()
                val rechargeViewModel: RechargeViewModel = viewModel()
                val consumerManagementViewModel: AdminConsumerManagementViewModel = viewModel()
                val consumerDetailsViewModel: AdminConsumerDetailsViewModel = viewModel()
                val adminChatViewModel: AdminChatViewModel = viewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    when (currentScreen) {

                        Screen.Splash -> SplashScreen(
                            onNavigateNext = { currentScreen = Screen.Onboarding },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.Onboarding -> OnboardingScreen(
                            onNextClick = { currentScreen = Screen.AccountSelection },
                            onSkipClick = { currentScreen = Screen.AccountSelection },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AccountSelection -> AccountSelectionScreen(
                            onConsumerClick = { currentScreen = Screen.ConsumerLogin },
                            onAdminClick = { currentScreen = Screen.AdminLogin },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.ConsumerLogin -> ConsumerLoginScreen(
                            onLoginClick = { email, consumerNo ->
                                loggedInUserEmail = email.trim().lowercase()
                                loggedInConsumerNo = consumerNo
                                currentScreen = Screen.Subscription
                            },
                            onSignUpClick = { currentScreen = Screen.ConsumerSignUp },
                            onForgotPasswordClick = {
                                entryPoint = Screen.ConsumerLogin
                                emailForReset = ""
                                verifiedOtp = ""
                                currentScreen = Screen.ForgotPassword
                            },
                            onBackClick = { currentScreen = Screen.AccountSelection },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.ConsumerSignUp -> ConsumerSignUpScreen(
                            onBackClick = { currentScreen = Screen.ConsumerLogin },
                            onLoginClick = { currentScreen = Screen.ConsumerLogin },
                            onCreateAccountSuccess = { currentScreen = Screen.ConsumerLogin },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.ForgotPassword -> ForgotPasswordScreen(
                            onBackClick = {
                                currentScreen = if (entryPoint == Screen.AdminLogin) {
                                    Screen.AdminLogin
                                } else {
                                    Screen.ConsumerLogin
                                }
                            },
                            onCodeSent = { email ->
                                emailForReset = email
                                currentScreen = Screen.VerifyResetCode
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.VerifyResetCode -> VerifyResetCodeScreen(
                            email = emailForReset,
                            onBackClick = { currentScreen = Screen.ForgotPassword },
                            onVerified = { otp ->
                                verifiedOtp = otp
                                currentScreen = Screen.ResetPassword
                            }
                        )

                        Screen.ResetPassword -> ResetPasswordScreen(
                            email = emailForReset,
                            otp = verifiedOtp,
                            onResetSuccess = {
                                currentScreen = if (entryPoint == Screen.AdminLogin) {
                                    Screen.AdminLogin
                                } else {
                                    Screen.ConsumerLogin
                                }
                            },
                            onBackClick = { currentScreen = Screen.VerifyResetCode },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.Dashboard -> DashboardScreen(
                            userEmail = loggedInUserEmail,
                            onLogout = {
                                loggedInUserEmail = ""
                                loggedInConsumerNo = ""
                                currentScreen = Screen.ConsumerLogin
                            },
                            onRechargeClick = { currentScreen = Screen.RechargePlans },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAddClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onAIClick = { currentScreen = Screen.AIAssistant },
                            onNotificationsClick = { currentScreen = Screen.NotificationPreferences },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.EnterMonthlyUsage -> EnterMonthlyUsageScreen(
                            userEmail = loggedInUserEmail,
                            onBackClick = { currentScreen = Screen.Dashboard },
                            onSaveClick = { currentScreen = Screen.Dashboard },
                            onPredictClick = { currentScreen = Screen.MonthlyUsageForecast },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.MonthlyUsageForecast -> MonthlyUsageForecastScreen(
                            userEmail = loggedInUserEmail,
                            onBackClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onChoosePlanClick = { currentScreen = Screen.RechargePlans },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.RechargePlans -> RechargePlansScreen(
                            onBackClick = { currentScreen = Screen.Dashboard },
                            onBuyClick = { currentScreen = Screen.PaymentReview },
                            onRecommendedClick = { currentScreen = Screen.SelectPlanDuration },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant },
                            viewModel = rechargeViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.SelectPlanDuration -> SelectPlanDurationScreen(
                            onBackClick = { currentScreen = Screen.RechargePlans },
                            onPlanSelect = { },
                            onPredictionClick = { currentScreen = Screen.RechargePlans },
                            onAIClick = { currentScreen = Screen.AIAssistant },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.PaymentReview -> PaymentReviewScreen(
                            onBackClick = { currentScreen = Screen.RechargePlans },
                            onPayNowClick = { currentScreen = Screen.RechargeOrderSummary },
                            onCardOptionClick = { currentScreen = Screen.CardDetails },
                            onNetBankingOptionClick = { currentScreen = Screen.NetBanking },
                            viewModel = rechargeViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.CardDetails -> CardDetailsScreen(
                            onBackClick = { currentScreen = Screen.PaymentReview },
                            onPaySecurelyClick = { currentScreen = Screen.RechargeOrderSummary },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.NetBanking -> NetBankingScreen(
                            onBackClick = { currentScreen = Screen.PaymentReview },
                            onProceedToPayClick = { currentScreen = Screen.RechargeOrderSummary },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.RechargeOrderSummary -> RechargeOrderSummaryScreen(
                            onBackClick = { currentScreen = Screen.PaymentReview },
                            onSecurePayClick = { },
                            viewModel = rechargeViewModel,
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AdminLogin -> AdminLoginScreen(
                            onBackClick = { currentScreen = Screen.AccountSelection },
                            onLoginSuccess = { email: String ->
                                loggedInAdminEmail = email.trim().lowercase()
                                currentScreen = Screen.AdminDashboard
                            },
                            onSignUpClick = { currentScreen = Screen.AdminSignUp },
                            onRecoveryClick = { email ->
                                emailForReset = email
                                verifiedOtp = ""
                                entryPoint = Screen.AdminLogin
                                currentScreen = Screen.ForgotPassword
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AdminSignUp -> AdminSignUpScreen(
                            onBackClick = { currentScreen = Screen.AccountSelection },
                            onLoginClick = { currentScreen = Screen.AdminLogin },
                            onSignUpSuccess = { email: String ->
                                loggedInAdminEmail = email.trim().lowercase()
                                currentScreen = Screen.AdminDashboard
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AdminDashboard -> AdminDashboardScreen(
                            onLogout = {
                                loggedInAdminEmail = ""
                                currentScreen = Screen.AdminLogin
                            },
                            onConsumersClick = { currentScreen = Screen.AdminConsumerManagement },
                            onSectorsClick = { currentScreen = Screen.AdminSectorAnalysis },
                            onReportsClick = { currentScreen = Screen.AdminReports },
                            onAddConsumerClick = { currentScreen = Screen.AddConsumer },
                            onProfileClick = { currentScreen = Screen.AdminProfile },
                            modifier = Modifier.padding(innerPadding)
                        )
                        Screen.AdminConsumerManagement -> ConsumerManagementScreen(
                            onBackClick = { currentScreen = Screen.AdminDashboard },
                            onOverviewClick = { currentScreen = Screen.AdminDashboard },
                            onConsumersClick = { currentScreen = Screen.AdminConsumerManagement },
                            onSectorsClick = { currentScreen = Screen.AdminSectorAnalysis },
                            onReportsClick = { currentScreen = Screen.AdminReports },
                            viewModel = consumerManagementViewModel,
                            onDetailClick = { consumerId ->
                                selectedConsumerId = consumerId
                                currentScreen = Screen.AdminConsumerDetails
                            },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AdminSectorAnalysis -> AdminSectorAnalysisScreen(
                            onBackClick = { currentScreen = Screen.AdminDashboard },
                            onOverviewClick = { currentScreen = Screen.AdminDashboard },
                            onConsumersClick = { currentScreen = Screen.AdminConsumerManagement },
                            onSectorsClick = { currentScreen = Screen.AdminSectorAnalysis },
                            onReportsClick = { currentScreen = Screen.AdminReports },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AdminConsumerDetails -> AdminConsumerDetailsScreen(
                            consumerNo = selectedConsumerId,
                            viewModel = consumerDetailsViewModel,
                            onBackClick = { currentScreen = Screen.AdminConsumerManagement },
                            onOverviewClick = { currentScreen = Screen.AdminDashboard },
                            onConsumersClick = { currentScreen = Screen.AdminConsumerManagement },
                            onSectorsClick = { currentScreen = Screen.AdminSectorAnalysis },
                            onReportsClick = { currentScreen = Screen.AdminReports },
                            onMessageClick = { currentScreen = Screen.AdminChat },
                            modifier = Modifier.padding(innerPadding)
                        )

                        Screen.AdminChat -> {
                            val consumerName = consumerDetailsViewModel.consumerDetails.collectAsState().value?.fullName ?: "Consumer"
                            AdminChatScreen(
                                consumerNo = selectedConsumerId,
                                consumerName = consumerName,
                                onBackClick = { currentScreen = Screen.AdminConsumerDetails },
                                viewModel = adminChatViewModel
                            )
                        }

                        Screen.AIAssistant -> AIAssistantScreen(
                            userEmail = loggedInUserEmail,
                            viewModel = aiViewModel.apply {
                                if (loggedInConsumerNo.isNotEmpty()) consumerNo = loggedInConsumerNo
                            },
                            onBackClick = { currentScreen = Screen.Dashboard },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile }
                        )

                        Screen.Profile -> ProfileScreen(
                            userEmail = loggedInUserEmail,
                            onLogoutClick = {
                                loggedInUserEmail = ""
                                loggedInConsumerNo = ""
                                currentScreen = Screen.ConsumerLogin
                            },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant },
                            onAccountDetailsClick = { currentScreen = Screen.AccountDetails },
                            onNotificationsClick = { currentScreen = Screen.NotificationPreferences },
                            onSavedDataClick = { currentScreen = Screen.DailyUsageHistory },
                            onPrivacyClick = { currentScreen = Screen.PrivacyPolicy }
                        )

                        Screen.PrivacyPolicy -> PrivacyPolicyScreen(
                            onBackClick = { currentScreen = Screen.Profile },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant }
                        )

                        Screen.DailyUsageHistory -> DailyUsageHistoryScreen(
                            userEmail = loggedInUserEmail,
                            onBackClick = { currentScreen = Screen.Profile },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant }
                        )

                        Screen.NotificationPreferences -> NotificationPreferencesScreen(
                            onBackClick = { currentScreen = Screen.Dashboard },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant }
                        )

                        Screen.AccountDetails -> AccountDetailsScreen(
                            userEmail = loggedInUserEmail,
                            onBackClick = { currentScreen = Screen.Profile },
                            onSaveSuccess = { currentScreen = Screen.Profile },
                            onHomeClick = { currentScreen = Screen.Dashboard },
                            onUsageClick = { currentScreen = Screen.EnterMonthlyUsage },
                            onPlansClick = { currentScreen = Screen.RechargePlans },
                            onProfileClick = { currentScreen = Screen.Profile },
                            onAIClick = { currentScreen = Screen.AIAssistant }
                        )

                        Screen.AddConsumer -> AddConsumerScreen(
                            onBackClick = { currentScreen = Screen.AdminDashboard },
                            onOverviewClick = { currentScreen = Screen.AdminDashboard },
                            onSectorsClick = { currentScreen = Screen.AdminSectorAnalysis },
                            onConsumersClick = { currentScreen = Screen.AdminConsumerManagement },
                            onReportsClick = { currentScreen = Screen.AdminReports },
                            onSuccess = { currentScreen = Screen.AdminDashboard }
                        )

                        Screen.AdminProfile -> AdminProfileScreen(
                            adminEmail = loggedInAdminEmail,
                            onBackClick = { currentScreen = Screen.AdminDashboard },
                            onLogoutClick = {
                                loggedInAdminEmail = ""
                                currentScreen = Screen.AdminLogin
                            }
                        )

                        Screen.AdminReports -> AdminReportsScreen(
                            onBackClick = { currentScreen = Screen.AdminDashboard },
                            onOverviewClick = { currentScreen = Screen.AdminDashboard },
                            onSectorsClick = { currentScreen = Screen.AdminSectorAnalysis },
                            onConsumersClick = { currentScreen = Screen.AdminConsumerManagement }
                        )

                        Screen.Subscription -> {
                            SubscriptionScreen(
                                onSkipClick = { currentScreen = Screen.Dashboard },
                                onSuccess = { currentScreen = Screen.Dashboard },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}