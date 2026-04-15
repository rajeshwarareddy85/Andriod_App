package com.simats.myapplication

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.*
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun EnterMonthlyUsageScreen(
    userEmail: String,
    onBackClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onPredictClick: () -> Unit = {},
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
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var usageInput by rememberSaveable { mutableStateOf("") }
    var latestSavedKwh by rememberSaveable { mutableStateOf<Double?>(null) }
    var latestSavedDate by rememberSaveable { mutableStateOf<String?>(null) }
    var latestIsYesterday by rememberSaveable { mutableStateOf(false) }
    var daysEntered by rememberSaveable { mutableStateOf(0) }
    var isSaving by rememberSaveable { mutableStateOf(false) }
    var isLoading by rememberSaveable { mutableStateOf(true) }
    var statusMessage by rememberSaveable { mutableStateOf("Loading consumer details...") }
    var consumerNo by rememberSaveable { mutableStateOf("") }

    val todayDate = remember { LocalDate.now().toString() }

    suspend fun loadScreenData() {
        try {
            isLoading = true
            statusMessage = "Loading consumer details..."

            if (userEmail.isBlank()) {
                consumerNo = ""
                daysEntered = 0
                latestSavedKwh = null
                latestSavedDate = null
                statusMessage = "User email is empty"
                return
            }

            val profileResponse = ApiClient.api.getUserProfileByEmail(userEmail)

            if (!profileResponse.isSuccessful) {
                consumerNo = ""
                daysEntered = 0
                latestSavedKwh = null
                latestSavedDate = null
                statusMessage = "Failed to load consumer profile"
                return
            }

            val profileBody = profileResponse.body()
            val fetchedConsumerNo = profileBody?.user?.consumerNo?.trim().orEmpty()

            if (profileBody == null || !profileBody.ok || fetchedConsumerNo.isBlank()) {
                consumerNo = ""
                daysEntered = 0
                latestSavedKwh = null
                latestSavedDate = null
                statusMessage = profileBody?.error ?: "Consumer number not found"
                return
            }

            consumerNo = fetchedConsumerNo
            statusMessage = "Consumer loaded successfully"

            val historyResponse = ApiClient.api.getDailyUsageHistory(consumerNo)

            if (!historyResponse.isSuccessful) {
                daysEntered = 0
                statusMessage = "Consumer loaded, but history fetch failed"
                return
            }

            val historyBody = historyResponse.body()

            if (historyBody != null && historyBody.ok) {
                val historyList = historyBody.history
                daysEntered = historyList.size.coerceAtMost(30)
                statusMessage = if (daysEntered >= 30) {
                    "30 days completed. Prediction is ready."
                } else {
                    "Daily tracking ready"
                }
            } else {
                daysEntered = 0
                statusMessage = historyBody?.error ?: "Could not load usage history"
            }

            // Fetch the latest saved reading (yesterday-first)
            val latestResponse = ApiClient.api.getLatestDailyUsage(consumerNo)
            val latestBody = latestResponse.body()
            if (latestResponse.isSuccessful && latestBody != null && latestBody.ok && latestBody.found) {
                latestSavedKwh = latestBody.usageKwh
                latestSavedDate = latestBody.usageDate
                latestIsYesterday = latestBody.isYesterday
            } else {
                latestSavedKwh = null
                latestSavedDate = null
                latestIsYesterday = false
            }
        } catch (e: Exception) {
            consumerNo = ""
            daysEntered = 0
            latestSavedKwh = null
            latestSavedDate = null
            statusMessage = e.message ?: "Network error while loading data"
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(userEmail) {
        loadScreenData()
    }

    val canEdit = !isLoading && consumerNo.isNotBlank()
    val canSave = canEdit && !isSaving
    val canPredict = !isLoading && consumerNo.isNotBlank() && daysEntered >= 30

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
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Enter Daily Usage",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "DAILY USAGE DATA (KWH)",
                color = textGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                CircularProgressIndicator(color = cyanColor)
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Loading consumer number...",
                    color = textGray,
                    fontSize = 12.sp
                )
            } else {
                // Latest saved reading value — prominent and visible
                val kwhDisplay = latestSavedKwh?.let { String.format("%.1f", it) } ?: "0.0"
                val dateLabel = when {
                    latestSavedDate == null -> "No previous data"
                    latestIsYesterday -> "Yesterday · $latestSavedDate"
                    else -> "Last recorded · $latestSavedDate"
                }

                Text(
                    text = "$kwhDisplay kWh",
                    color = if (latestSavedKwh != null) cyanColor else textGray,
                    fontSize = 60.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = dateLabel,
                    color = textGray,
                    fontSize = 12.sp
                )

                Text(
                    text = "Yesterday's Usage",
                    color = textGray.copy(alpha = 0.6f),
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = usageInput,
                onValueChange = { newValue ->
                    if (newValue.count { it == '.' } <= 1) {
                        usageInput = newValue
                    }
                },
                label = { Text("Enter usage in KWh") },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = cyanColor
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                modifier = Modifier.fillMaxWidth(),
                enabled = canEdit,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = cyanColor,
                    unfocusedBorderColor = Color(0xFF1B3B40),
                    focusedLabelColor = cyanColor,
                    unfocusedLabelColor = textGray,
                    cursorColor = cyanColor,
                    focusedContainerColor = cardBgColor,
                    unfocusedContainerColor = cardBgColor,
                    disabledTextColor = Color.White.copy(alpha = 0.7f),
                    disabledBorderColor = Color(0xFF1B3B40),
                    disabledLabelColor = textGray,
                    disabledContainerColor = cardBgColor
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Usage date: $todayDate",
                color = textGray,
                fontSize = 12.sp
            )

            Text(
                text = "Store time is handled by backend automatically",
                color = textGray,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(22.dp))

            Surface(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onAIClick() },
                color = Color.Transparent,
                border = BorderStroke(1.dp, Color(0xFF1B3B40)),
                shape = CircleShape
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "SMART PREDICTION ASSIST",
                        color = cyanColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(cardBgColor)
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Current Period",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Current month daily tracking",
                            color = textGray,
                            fontSize = 12.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Days Recorded: $daysEntered/30",
                            color = if (daysEntered >= 30) Color(0xFF4CAF50) else cyanColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = statusMessage,
                            color = textGray,
                            fontSize = 11.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Consumer No",
                            color = textGray,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (consumerNo.isBlank()) "--" else consumerNo,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        val usageValue = usageInput.toDoubleOrNull()

                        if (consumerNo.isBlank()) {
                            Toast.makeText(context, "Consumer number not loaded", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (usageInput.isBlank()) {
                            Toast.makeText(context, "Please enter usage", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (usageValue == null) {
                            Toast.makeText(context, "Enter valid integer or decimal value", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (usageValue < 0) {
                            Toast.makeText(context, "Usage cannot be negative", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        scope.launch {
                            try {
                                isSaving = true
                                statusMessage = "Saving daily usage..."

                                val request = SaveDailyUsageRequest(
                                    consumerNo = consumerNo,
                                    usageDate = todayDate,
                                    usageKwh = usageValue
                                )

                                val response = ApiClient.api.saveDailyUsage(request)
                                val body = response.body()

                                if (response.isSuccessful && body != null && body.ok) {
                                    usageInput = ""

                                    Toast.makeText(
                                        context,
                                        body.message ?: "Saved successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                    loadScreenData()
                                    onSaveClick()
                                } else {
                                    statusMessage = body?.error ?: "Save failed"
                                    Toast.makeText(
                                        context,
                                        body?.error ?: "Save failed",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                statusMessage = e.message ?: "Network error"
                                Toast.makeText(
                                    context,
                                    e.message ?: "Network error",
                                    Toast.LENGTH_LONG
                                ).show()
                            } finally {
                                isSaving = false
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    enabled = canSave,
                    colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isSaving) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.Black,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = "Save Reading",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Button(
                    onClick = onPredictClick,
                    enabled = canPredict,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF142426),
                        disabledContainerColor = Color(0xFF142426).copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Predict Next Mo.",
                        color = if (canPredict) Color.White else Color.White.copy(alpha = 0.5f),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
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
            onAIClick = onAIClick,
            selectedTab = "usage"
        )
    }
}


@Preview(showBackground = true)
@Composable
fun EnterMonthlyUsageScreenPreview() {
    EnterMonthlyUsageScreen(
        userEmail = "demo@gmail.com"
    )
}