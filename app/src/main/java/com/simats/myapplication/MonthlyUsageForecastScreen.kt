package com.simats.myapplication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.*
import com.simats.myapplication.network.ApiClient
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.entry.entryModelOf
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.compose.chart.line.lineSpec
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.component.lineComponent
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import kotlin.math.roundToInt
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun MonthlyUsageForecastScreen(
    userEmail: String,
    onBackClick: () -> Unit = {},
    onChoosePlanClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    val cardBgColor = Color(0xFF142426)

    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val scope = rememberCoroutineScope()

    var isLoading by rememberSaveable { mutableStateOf(true) }
    var statusMessage by rememberSaveable { mutableStateOf("Loading analytics...") }
    var consumerNo by rememberSaveable { mutableStateOf("") }

    var totalPredictedKwh by rememberSaveable { mutableStateOf<Double?>(null) }
    var totalPredictedUnits by rememberSaveable { mutableStateOf<Double?>(null) }
    var dailyUsageHistory by remember { mutableStateOf(listOf<Double>()) }
    var weeklyTotals by remember { mutableStateOf(listOf<Double>()) }
    var confidenceScore by rememberSaveable { mutableStateOf<Double?>(null) }

    var selectedView by rememberSaveable { mutableStateOf("Daily") }

    suspend fun loadPrediction() {
        try {
            isLoading = true
            statusMessage = "Syncing with grid data..."

            val profileResponse = ApiClient.api.getUserProfileByEmail(userEmail)
            val profileBody = profileResponse.body()
            val fetchedConsumerNo = profileBody?.user?.consumerNo?.trim().orEmpty()

            if (!profileResponse.isSuccessful || profileBody == null || !profileBody.ok || fetchedConsumerNo.isBlank()) {
                statusMessage = profileBody?.error ?: "Consumer ID mismatch"
                return
            }

            consumerNo = fetchedConsumerNo
            
            val response = ApiClient.api.predictNext30FromDb(fetchedConsumerNo)
            val body = response.body()

            if (response.isSuccessful && body != null && body.ok) {
                totalPredictedKwh = body.predictionSummary?.nextMonthUsageKwh
                totalPredictedUnits = body.predictionSummary?.nextMonthUsageUnits ?: totalPredictedKwh

                // 1. Fetch Current Month Daily Trend
                val dailyTrendRes = ApiClient.api.getCurrentMonthDailyTrend(fetchedConsumerNo)
                if (dailyTrendRes.isSuccessful && dailyTrendRes.body()?.ok == true) {
                    dailyUsageHistory = dailyTrendRes.body()?.dailyTrend?.map { it.usage ?: 0.0 } ?: emptyList()
                }

                // 2. Fetch Current Month Weekly Trend
                val weeklyTrendRes = ApiClient.api.getCurrentMonthWeeklyTrend(fetchedConsumerNo)
                if (weeklyTrendRes.isSuccessful && weeklyTrendRes.body()?.ok == true) {
                    weeklyTotals = weeklyTrendRes.body()?.weeklyTrend?.map { it.usage ?: 0.0 } ?: emptyList()
                }

                if (dailyUsageHistory.isNotEmpty()) {
                    val max = dailyUsageHistory.maxOrNull() ?: 1.0
                    val min = dailyUsageHistory.minOrNull() ?: 0.0
                    val variance = if (max > 0) ((max - min) / max) * 100 else 0.0
                    confidenceScore = (98.0 - (variance * 0.15)).coerceIn(82.0, 98.9)
                }

                statusMessage = body.message ?: "Analysis complete"
            } else {
                statusMessage = body?.error ?: "Prediction failed. Please try again."
            }
        } catch (e: Exception) {
            statusMessage = e.message?.take(120) ?: "Network error. Check your connection."
        } finally {
            isLoading = false
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, userEmail) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (userEmail.isNotBlank()) {
                    scope.launch { loadPrediction() }
                } else {
                    isLoading = false
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val unitsText = totalPredictedUnits?.roundToInt()?.let { "$it kWh" } ?: "-- kWh"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(48.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.Default.KeyboardArrowLeft, "Back", tint = Color.White)
                }
                Text(
                    "Prediction Analysis",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // PREMIUM HEADER CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(32.dp))
                    .background(cardBgColor)
                    .padding(28.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = unitsText,
                            color = Color.White,
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text("Actual Usage vs. Predicted Curve", color = textGray.copy(alpha = 0.7f), fontSize = 14.sp)

                    Spacer(modifier = Modifier.height(32.dp))

                    if (isLoading) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(200.dp)) {
                            CircularProgressIndicator(color = cyanColor)
                        }
                    } else if (dailyUsageHistory.isEmpty()) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth().height(200.dp)) {
                            Text("No usage data available in the database.", color = textGray, fontSize = 14.sp)
                        }
                    } else {
                        val graphValues = if (selectedView == "Daily") dailyUsageHistory else weeklyTotals
                        PredictionCurveGraph(
                            values = graphValues,
                            activeColor = cyanColor,
                            label = if (selectedView == "Daily") "DAY" else "WEEK"
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Consumer No", color = textGray, fontSize = 12.sp)
                            Text(consumerNo.ifEmpty { "Pending..." }, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                        Row(modifier = Modifier.clip(CircleShape).background(Color(0xFF1B3B40)).padding(4.dp)) {
                            listOf("Daily", "Weekly").forEach { view ->
                                Box(
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .background(if (selectedView == view) cyanColor else Color.Transparent)
                                        .clickable { selectedView = view }
                                        .padding(horizontal = 16.dp, vertical = 6.dp)
                                ) {
                                    Text(view, color = if (selectedView == view) Color.Black else Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // STATS SECTION
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                val avgDailyText = totalPredictedKwh?.let { "${String.format("%.1f", it/30)} kWh" } ?: "-- kWh"
                val confText = confidenceScore?.let { "${String.format("%.1f", it)}%" } ?: "--%"
                PredictionStatCard(Modifier.weight(1f), "Avg. Daily", avgDailyText, cyanColor)
                PredictionStatCard(Modifier.weight(1f), "Confidence", confText, Color.White)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { scope.launch { loadPrediction() } },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B3B40))
            ) {
                Text("Refresh Analysis", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onChoosePlanClick,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cyanColor)
            ) {
                Icon(Icons.Default.FlashOn, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recommended Plan", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // FLOATING AI ACTION
        ProfileBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            cyanColor = cyanColor,
            textGray = textGray,
            darkBgColor = darkBgColor,
            onHomeClick = onHomeClick,
            onUsageClick = onUsageClick,
            onPlansClick = onPlansClick,
            onProfileClick = onProfileClick,
            onAIClick = onAIClick,
            selectedTab = "usage"
        )
    }
}

@Composable
private fun PredictionCurveGraph(
    values: List<Double>,
    activeColor: Color,
    label: String
) {
    if (values.isEmpty()) return

    val modelProducer = remember { ChartEntryModelProducer() }

    LaunchedEffect(values) {
        val entries = values.mapIndexed { index, value -> FloatEntry(index.toFloat(), value.toFloat()) }
        modelProducer.setEntries(entries)
    }

    Chart(
        chart = lineChart(
            lines = listOf(
                lineSpec(
                    lineColor = Color.White,
                    lineThickness = 3.dp,
                    lineBackgroundShader = DynamicShaders.fromBrush(
                        Brush.verticalGradient(listOf(activeColor.copy(alpha = 0.4f), Color.Transparent))
                    )
                ),
                lineSpec(
                    lineColor = activeColor,
                    lineThickness = 2.dp
                )
            )
        ),
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(label = null, guideline = null, tick = null),
        bottomAxis = rememberBottomAxis(
            label = textComponent(
                color = Color.White.copy(alpha = 0.6f),
                textSize = 10.sp
            ),
            guideline = lineComponent(
                color = Color.White.copy(alpha = 0.2f),
                thickness = 1.dp
            ),
            tick = null,
            valueFormatter = { value, _ -> "$label ${(value + 1).toInt()}" },
            itemPlacer = AxisItemPlacer.Horizontal.default(spacing = 1)
        ),
        modifier = Modifier.fillMaxWidth().height(220.dp)
    )
}

@Composable
private fun PredictionStatCard(modifier: Modifier, label: String, value: String, color: Color) {
    Box(modifier = modifier.clip(RoundedCornerShape(24.dp)).background(Color(0xFF142426)).padding(20.dp)) {
        Column {
            Text(label, color = Color(0xFF8B9D9F), fontSize = 12.sp)
            Text(value, color = color, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

