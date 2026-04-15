package com.simats.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DashboardScreen(
    userEmail: String = "",
    onLogout: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onRechargeClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)

    var consumerNo by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("") }
    var thisMonthTotal by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("--") }
    var remainingDays by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("--") }
    var todayCost by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("--") }
    var forecastVal by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("--") }
    var unitCost by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf("--") }
    var isLoading by androidx.compose.runtime.saveable.rememberSaveable { androidx.compose.runtime.mutableStateOf(true) }
    var weeklyTrend by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList<com.simats.myapplication.model.DailyTrendItem>()) }

    var showAiPopup by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }
    var activePlan by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<com.simats.myapplication.model.ActivePlanDetails?>(null) }
    var alerts by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(emptyList<com.simats.myapplication.model.AlertItem>()) }
    var hasUnreadMessages by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(userEmail) {
        if (userEmail.isNotBlank()) {
            isLoading = true
            try {
                val profileRes = com.simats.myapplication.network.ApiClient.api.getUserProfileByEmail(userEmail)
                if (profileRes.isSuccessful && profileRes.body()?.ok == true) {
                    val cNo = profileRes.body()?.user?.consumerNo ?: ""
                    consumerNo = cNo
                    
                    if (cNo.isNotBlank()) {
                        // 1. Fetch dashboard summary
                        val summaryRes = com.simats.myapplication.network.ApiClient.api.getDashboardSummary(cNo)
                        if (summaryRes.isSuccessful && summaryRes.body()?.ok == true) {
                            val data = summaryRes.body()!!
                            val mTotal = data.thisMonthTotalKwh
                            thisMonthTotal = if (mTotal != null) String.format(java.util.Locale.US, "%.1f", mTotal) else "0.0"
                            val tCost = data.todayCost
                            todayCost = if (tCost != null) String.format(java.util.Locale.US, "%.1f", tCost) else "0.0"
                            remainingDays = data.remainingDays?.toString() ?: "--"
                        }
                        
                        // 2. Fetch forecast
                        val forecastRes = com.simats.myapplication.network.ApiClient.api.predictNext30FromDb(cNo)
                        if (forecastRes.isSuccessful && forecastRes.body()?.ok == true) {
                            val predVal = forecastRes.body()?.predictionSummary?.nextMonthUsageKwh
                            if (predVal != null) {
                                val floatVal = predVal.toString().toFloatOrNull()
                                if (floatVal != null) {
                                    forecastVal = String.format(java.util.Locale.US, "%.0f", floatVal)
                                }
                            }
                        }
                        
                        // 3. Fetch weekly trend
                        val weeklyRes = com.simats.myapplication.network.ApiClient.api.getWeeklyTrend(cNo)
                        if (weeklyRes.isSuccessful && weeklyRes.body()?.ok == true) {
                            weeklyTrend = weeklyRes.body()?.weeklyUsage ?: emptyList()
                        }

                        // 4. Fetch Unit Cost
                        val costRes = com.simats.myapplication.network.ApiClient.api.getUnitCost()
                        if (costRes.isSuccessful && costRes.body()?.ok == true) {
                            val cost = costRes.body()?.oneUnitCost
                            unitCost = if (cost != null) String.format(java.util.Locale.US, "%.0f", cost) else "10"
                        }

                        // 5. Fetch Active Plan
                        val planRes = com.simats.myapplication.network.ApiClient.api.getActivePlanSummary(cNo)
                        if (planRes.isSuccessful && planRes.body()?.ok == true) {
                            activePlan = planRes.body()?.plan
                        }

                        // 6. Fetch Alerts
                        val alertRes = com.simats.myapplication.network.ApiClient.api.getAlerts(cNo)
                        if (alertRes.isSuccessful && alertRes.body()?.ok == true) {
                            alerts = alertRes.body()?.alerts ?: emptyList()
                        }

                        // 7. Check for unread admin messages
                        val chatRes = com.simats.myapplication.network.ApiClient.api.getChatHistory(cNo)
                        if (chatRes.isSuccessful && chatRes.body()?.ok == true) {
                            val history = chatRes.body()?.history ?: emptyList()
                            hasUnreadMessages = history.any { it.senderRole == "ADMIN" && !it.isRead }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    // -- AUTO-SHOW AI WELCOME POPUP ON ENTRY --
    androidx.compose.runtime.LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(800)
        showAiPopup = true
        kotlinx.coroutines.delay(2000)
        showAiPopup = false
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(28.dp)) // Equal space to balance the notification icon

                Text(
                    text = "Energy Hub",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                Box {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = Color.White,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { onNotificationsClick() }
                    )
                    if (hasUnreadMessages) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.TopEnd)
                                .border(1.5.dp, darkBgColor, CircleShape)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // -- ACTIVE PLAN STATUS --
            if (activePlan != null) {
                ActivePlanSection(activePlan!!)
                Spacer(modifier = Modifier.height(24.dp))
            }

            // -- ALERTS SECTION --
            if (alerts.isNotEmpty()) {
                AlertsSection(alerts)
                Spacer(modifier = Modifier.height(24.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(cyanColor)
                        .clickable { onAddClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "ADD USAGE",
                            color = Color.Black,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF142C2E))
                        .border(1.dp, Color(0xFF1E5154), RoundedCornerShape(12.dp))
                        .clickable { onAIClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = cyanColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI ASSISTANT",
                            color = cyanColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "CURRENT STATUS",
                color = textGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                if (isLoading) {
                    androidx.compose.material3.CircularProgressIndicator(color = cyanColor, modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DashboardCard(
                            modifier = Modifier.weight(1f),
                            title = "THIS MONTH",
                            value = thisMonthTotal,
                            unit = "kWh",
                            icon = Icons.Default.CalendarMonth
                        )
                        DashboardCard(
                            modifier = Modifier.weight(1f),
                            title = "CURRENT",
                            value = unitCost,
                            unit = "₹ / unit",
                            icon = Icons.Default.FlashOn
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        DashboardCard(
                            modifier = Modifier.weight(1f),
                            title = "REMAINING",
                            value = remainingDays,
                            unit = "Days",
                            icon = Icons.Default.Timer
                        )
                        DashboardCard(
                            modifier = Modifier.weight(1f),
                            title = "FORECAST",
                            value = forecastVal,
                            unit = "kWh",
                            icon = Icons.Default.BarChart
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            UsageTrendSection(textGray = textGray, cyanColor = cyanColor, weeklyTrend = weeklyTrend)

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0F2224))
                    .border(1.dp, Color(0xFF163C3F), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.WarningAmber,
                            contentDescription = null,
                            tint = cyanColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "RECHARGE SOON!",
                            color = cyanColor,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Your current plan ends on March 30",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(cyanColor)
                            .clickable { onRechargeClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "RECHARGE NOW",
                            color = Color.Black,
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        // -- AI POPUP --
        if (showAiPopup) {
            androidx.compose.runtime.LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                showAiPopup = false
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 90.dp, end = 20.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 16.dp))
                    .background(cyanColor)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
            ) {
                Text(
                    "How may I help you?",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        ProfileBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            cyanColor = cyanColor,
            textGray = textGray,
            darkBgColor = darkBgColor,
            onHomeClick = onHomeClick,
            onUsageClick = onUsageClick,
            onPlansClick = onPlansClick,
            onProfileClick = onProfileClick,
            onAIClick = { 
                onAIClick() 
            },
            selectedTab = "home"
        )
    }
}

@Composable
fun DashboardCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    unit: String = "",
    icon: ImageVector
) {
    val cardBgColor = Color(0xFF142426)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)


    Box(
        modifier = modifier
            .height(115.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(cardBgColor)
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(12.dp))
            .padding(14.dp)
    ) {
        Column(verticalArrangement = Arrangement.SpaceBetween) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = cyanColor,
                modifier = Modifier.size(20.dp)
            )

            Column {
                Text(
                    text = title,
                    color = textGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = value,
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (unit.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = unit,
                            color = textGray,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(bottom = 3.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UsageTrendSection(textGray: Color, cyanColor: Color, weeklyTrend: List<com.simats.myapplication.model.DailyTrendItem> = emptyList()) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF142426))
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Usage Trend",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    color = Color(0xFF1B3B40),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = "7 DAY VIEW",
                        color = cyanColor,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxUsageRaw = weeklyTrend.maxOfOrNull { it.usage ?: 0.0 } ?: 0.0
                val maxUsage = if (maxUsageRaw > 0) maxUsageRaw else 1.0

                val chartData = if (weeklyTrend.size == 7) weeklyTrend else List(7) {
                    com.simats.myapplication.model.DailyTrendItem("", listOf("MON","TUE","WED","THU","FRI","SAT","SUN")[it], 0.0)
                }

                chartData.forEachIndexed { index, item ->
                    val valueFloat = ((item.usage ?: 0.0) / maxUsage).toFloat().coerceIn(0.02f, 1f)
                    val isYesterday = index == 5

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        if (isYesterday) {
                            Surface(
                                color = cyanColor,
                                shape = RoundedCornerShape(4.dp),
                                modifier = Modifier.padding(bottom = 8.dp)
                            ) {
                                Text(
                                    text = String.format(java.util.Locale.US, "%.1f", item.usage ?: 0.0),
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .fillMaxHeight(valueFloat)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                .background(if (isYesterday) cyanColor else Color(0xFF1B5E63))
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val chartData = if (weeklyTrend.size == 7) weeklyTrend else List(7) {
                    com.simats.myapplication.model.DailyTrendItem("", listOf("MON","TUE","WED","THU","FRI","SAT","SUN")[it], 0.0)
                }

                chartData.forEach { item ->
                    Text(
                        text = item.day?.uppercase() ?: "---",
                        color = textGray.copy(alpha = 0.7f),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen()
}

@Composable
fun ActivePlanSection(plan: com.simats.myapplication.model.ActivePlanDetails) {
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF142426))
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(20.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ElectricBolt, null, tint = cyanColor, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("Active Plan: ${plan.planName}", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Remaining", color = textGray, fontSize = 12.sp)
                    Text("${plan.remainingUnits?.toInt() ?: 0} kWh", color = cyanColor, fontSize = 20.sp, fontWeight = FontWeight.Black)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("Valid Until", color = textGray, fontSize = 12.sp)
                    Text(plan.expiryDate ?: "--", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            val total = plan.totalUnits ?: 1.0
            val rem = plan.remainingUnits ?: 0.0
            val progress = (rem / total).coerceIn(0.0, 1.0).toFloat()
            Box(modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape).background(Color(0xFF1B3B3E))) {
                Box(modifier = Modifier.fillMaxWidth(progress).fillMaxHeight().background(cyanColor))
            }
        }
    }
}

@Composable
fun AlertsSection(alerts: List<com.simats.myapplication.model.AlertItem>) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        for (alert in alerts.take(2)) {
            Surface(
                color = if (alert.type == "EXHAUSTED") Color(0xFF3B1B1B) else Color(0xFF3B331B),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (alert.type == "EXHAUSTED") Color(0xFFFF4444) else Color(0xFFFFBB33))
            ) {
                Row(modifier = Modifier.padding(12.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        if (alert.type == "EXHAUSTED") Icons.Default.Error else Icons.Default.Warning,
                        null,
                        tint = if (alert.type == "EXHAUSTED") Color(0xFFFF4444) else Color(0xFFFFBB33),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(alert.title ?: "Alert", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text(alert.message ?: "", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}