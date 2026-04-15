package com.simats.PowerPulse

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.PowerPulse.viewmodel.AdminConsumerDetailsViewModel
import com.simats.PowerPulse.model.DailyUsagePoint

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminConsumerDetailsScreen(
    consumerNo: String,
    viewModel: AdminConsumerDetailsViewModel,
    onBackClick: () -> Unit = {},
    onOverviewClick: () -> Unit = {},
    onSectorsClick: () -> Unit = {},
    onConsumersClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onMessageClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    val cardBg = Color(0xFF142426)
    val borderStroke = Color(0xFF1B3B3E)

    val context = LocalContext.current
    val consumerDetails by viewModel.consumerDetails.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val isDeleted by viewModel.isDeleted.collectAsState()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(consumerNo) {
        viewModel.fetchConsumerDetails(consumerNo)
    }

    LaunchedEffect(isDeleted) {
        if (isDeleted) {
            Toast.makeText(context, "Consumer deleted successfully", Toast.LENGTH_SHORT).show()
            viewModel.resetState()
            onBackClick()
        }
    }

    LaunchedEffect(error) {
        error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    Box(modifier = modifier.fillMaxSize().background(darkBgColor)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
                .padding(bottom = 80.dp) // leave space for bottom nav
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Header
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Consumer Details",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading && consumerDetails == null) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = cyanColor)
                }
            } else if (consumerDetails != null) {
                val data = consumerDetails!!

                // Profile Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBg)
                        .border(1.dp, borderStroke, RoundedCornerShape(20.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(80.dp).clip(CircleShape).border(2.dp, cyanColor, CircleShape)) {
                                Box(modifier = Modifier.fillMaxSize().background(Color(0xFF1B3B3E)), contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = cyanColor, modifier = Modifier.size(40.dp))
                                }
                                // Active/Online dot
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50))
                                        .border(2.dp, cardBg, CircleShape)
                                        .align(Alignment.BottomEnd)
                                        .offset(x = (-4).dp, y = (-4).dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(20.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = data.fullName,
                                        color = Color.White,
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        lineHeight = 26.sp
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    val statusColor = if (data.remainingDays < 3) Color(0xFFF44336) else cyanColor
                                    Surface(
                                        color = statusColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(6.dp),
                                        border = androidx.compose.foundation.BorderStroke(0.5.dp, statusColor.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = if (data.remainingDays < 3) "URGENT\nRECHARGE" else "ACCOUNT\nSTABLE",
                                            color = statusColor,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                            lineHeight = 11.sp
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Meter #${data.consumerNo}",
                                    color = textGray,
                                    fontSize = 14.sp
                                )
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    BadgeItem(data.mandal, Color(0xFF006064).copy(alpha = 0.5f), cyanColor)
                                    BadgeItem("Active", Color(0xFF263238), textGray)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            DetailStatBox("REMAINING DAYS", "${data.remainingDays} Days", if (data.remainingDays < 3) Color(0xFFF44336) else cyanColor, modifier = Modifier.weight(1f))
                            DetailStatBox("CONSUMER ID", data.consumerNo, Color.White, modifier = Modifier.weight(1f))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Usage Trend Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Usage Trend", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LegendItem("Daily kWh", cyanColor)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBg)
                        .border(1.dp, borderStroke, RoundedCornerShape(20.dp))
                        .padding(16.dp)
                ) {
                    if (data.dailyUsage.isNotEmpty()) {
                        UsageTrendGraph(data.dailyUsage, cyanColor, textGray)
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No usage history found", color = textGray, fontSize = 14.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // AI Insight Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF0C2426))
                    .border(1.dp, cyanColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(20.dp)
            ) {
                Row {
                    Box(
                        modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(cyanColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Lightbulb, null, tint = Color.Black, modifier = Modifier.size(24.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = "PowerPulse AI Insight", color = cyanColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Historical data shows peak usage during weekends. Recommend notifying user about off-peak saving plans.",
                            color = textGray,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = onMessageClick,
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B3E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.ChatBubble, null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Message", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier.weight(1.3f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                    } else {
                        Icon(Icons.Default.Delete, null, tint = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Remove Customer", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Bottom nav sits inside the Box so .align() is valid
        AdminBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onOverviewClick = onOverviewClick,
            onSectorsClick = onSectorsClick,
            onConsumersClick = onConsumersClick,
            onReportsClick = onReportsClick,
            selectedTab = "Consumers"
        )
    }

    // Dialog shown as overlay outside the Box
    if (showDeleteDialog && consumerDetails != null) {
        RemoveCustomerDialog(
            customerName = consumerDetails!!.fullName,
            onDismiss = { showDeleteDialog = false },
            onConfirm = {
                showDeleteDialog = false
                viewModel.deleteConsumer(consumerNo)
            }
        )
    }
}

@Composable
fun UsageTrendGraph(data: List<DailyUsagePoint>, graphColor: Color, labelColor: Color) {
    val maxUsage = data.maxOfOrNull { it.usage } ?: 1.0
    val displayMax = if (maxUsage > 0) maxUsage * 1.2 else 10.0

    Column(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier.weight(1f).fillMaxWidth()) {
            val width = size.width
            val height = size.height
            val spacing = width / (data.size - 1).coerceAtLeast(1)

            val path = Path()
            data.forEachIndexed { index, point ->
                val x = index * spacing
                val y = height - (point.usage / displayMax * height).toFloat()
                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = graphColor,
                style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            data.forEachIndexed { index, point ->
                // Show only a few date labels to avoid overlap
                if (index % 3 == 0 || index == data.size - 1) {
                    val dateLabel = point.date.split("-").lastOrNull() ?: ""
                    Text(text = dateLabel, color = labelColor, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
fun RemoveCustomerDialog(
    customerName: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val cardBg = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)
    val redColor = Color(0xFFD32F2F)

    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(24.dp),
            color = cardBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B3B3E))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(redColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(redColor.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = redColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Remove Customer Account?",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = androidx.compose.ui.text.buildAnnotatedString {
                        append("Are you sure you want to remove ")
                        withStyle(style = androidx.compose.ui.text.SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
                            append("$customerName's")
                        }
                        append(" account? This action will permanently delete their usage history and meter data. This cannot be undone.")
                    },
                    color = textGray,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = redColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Yes, Remove Account", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1B2B3E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Cancel", color = textGray, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BadgeItem(text: String, containerColor: Color, contentColor: Color) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(6.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        )
    }
}

@Composable
fun DetailStatBox(label: String, value: String, valueColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0C1D20).copy(alpha = 0.5f))
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(text = label, color = Color(0xFF8B9D9F), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = value, color = valueColor, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = label, color = Color(0xFF8B9D9F), fontSize = 12.sp)
    }
}
