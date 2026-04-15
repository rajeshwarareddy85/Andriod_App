package com.simats.PowerPulse

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.simats.PowerPulse.viewmodel.AdminDashboardViewModel
import com.simats.PowerPulse.viewmodel.AdminOverviewState
import java.text.DecimalFormat

@Composable
fun AdminDashboardScreen(
    viewModel: AdminDashboardViewModel = viewModel(),
    onLogout: () -> Unit = {},
    onOverviewClick: () -> Unit = {},
    onSectorsClick: () -> Unit = {},
    onConsumersClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onAddConsumerClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)

    val overviewState by viewModel.overviewState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.fetchDashboardOverview()
    }

    // Default static fallbacks while loading
    var currentDemandStr = "..."
    var totalConsumersStr = "..."
    var avgUnitsStr = "..."
    var predictedDemandStr = "..."
    
    val formatter = DecimalFormat("#,###")

    when (val state = overviewState) {
        is AdminOverviewState.Success -> {
            currentDemandStr = formatter.format(state.data.currentMonthTotalDemand)
            totalConsumersStr = formatter.format(state.data.totalConsumers)
            avgUnitsStr = formatter.format(state.data.avgUnitsPerUser)
            predictedDemandStr = formatter.format(state.data.predictedNextMonthDemand)
        }
        else -> {
            // Loading or Error leaves defaults as "..."
            // You could show a specialized error banner here if `state is AdminOverviewState.Error`
        }
    }

    var showActivity by remember { mutableStateOf(false) }

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
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF1B3B3E).copy(alpha = 0.5f))
                        .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "PowerPulse ",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Admin",
                        color = cyanColor,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications",
                    tint = textGray,
                    modifier = Modifier.size(26.dp)
                )

                Spacer(modifier = Modifier.width(20.dp))

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B3B3E).copy(alpha = 0.5f))
                        .border(1.dp, Color(0xFF1B3B3E), CircleShape)
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = cyanColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            MainEnergyDemandCard(cyanColor, textGray, currentDemandStr)

            Spacer(modifier = Modifier.height(24.dp))

            SecondaryStatsSection(cyanColor, textGray, totalConsumersStr, avgUnitsStr, predictedDemandStr)

            Spacer(modifier = Modifier.height(32.dp))

            AdminDemandSupplyChart(cyanColor, textGray)

            Spacer(modifier = Modifier.height(32.dp))

            AdminRecentActivitySection(
                cyanColor = cyanColor,
                textGray = textGray,
                showActivity = showActivity,
                onViewAllClick = { showActivity = !showActivity }
            )

            Spacer(modifier = Modifier.height(100.dp))
        }
        AdminBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onOverviewClick = onOverviewClick,
            onSectorsClick = onSectorsClick,
            onConsumersClick = onConsumersClick,
            onReportsClick = onReportsClick,
            selectedTab = "Overview"
        )
    }
}

@Composable
fun MainEnergyDemandCard(cyanColor: Color, textGray: Color, currentDemand: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF142426))
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(20.dp))
            .padding(24.dp)
    ) {
        Icon(
            imageVector = Icons.Default.FlashOn,
            contentDescription = null,
            tint = Color(0xFF1B3B3E).copy(alpha = 0.5f),
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterEnd)
                .offset(x = 20.dp)
                .rotate(-15f)
        )

        Column {
            Text(
                text = "TOTAL ENERGY DEMAND (CURRENT MONTH)",
                color = textGray,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = currentDemand,
                    color = Color.White,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "ku h",
                    color = cyanColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "4.2%",
                        color = Color(0xFF4CAF50),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun SecondaryStatsSection(
    cyanColor: Color, 
    textGray: Color, 
    totalConsumers: String, 
    avgUnits: String, 
    predictedDemand: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        AdminStatItem(
            title = "Total Consumers",
            value = totalConsumers,
            trend = "+2.4% from last month",
            trendColor = Color(0xFF4CAF50),
            icon = Icons.Default.Groups,
            cyanColor = cyanColor,
            textGray = textGray
        )

        AdminStatItem(
            title = "Avg Units / User",
            value = avgUnits,
            unit = "ku h",
            trend = "+1.1% efficiency",
            trendColor = Color(0xFF4CAF50),
            icon = Icons.Default.FlashOn,
            cyanColor = cyanColor,
            textGray = textGray
        )

        AdminStatItem(
            title = "Predicted Demand",
            value = predictedDemand,
            unit = "ku h",
            trend = "High demand forecast",
            trendColor = Color(0xFFFF9800),
            icon = Icons.Default.AutoGraph,
            cyanColor = cyanColor,
            textGray = textGray
        )
    }
}

@Composable
fun AdminStatItem(
    title: String,
    value: String,
    unit: String = "",
    trend: String,
    trendColor: Color,
    icon: ImageVector,
    cyanColor: Color,
    textGray: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF142426))
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = textGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = cyanColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = unit,
                        color = textGray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = trend,
                color = trendColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun AdminDemandSupplyChart(cyanColor: Color, textGray: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF142426))
            .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(20.dp))
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Demand vs Supply",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Historical grid efficiency\nfor the last 4 months",
                        color = textGray,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(cyanColor)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Demand",
                        color = textGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(textGray.copy(alpha = 0.5f))
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Supply",
                        color = textGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val months = listOf("MAY", "JUN", "JUL", "AUG")
                val demandValues = listOf(0.4f, 0.6f, 0.8f, 0.7f)
                val supplyValues = listOf(0.5f, 0.55f, 0.85f, 0.75f)

                months.forEachIndexed { index, month ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(contentAlignment = Alignment.BottomCenter) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Box(
                                    modifier = Modifier
                                        .width(12.dp)
                                        .fillMaxHeight(demandValues[index])
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 4.dp,
                                                topEnd = 4.dp
                                            )
                                        )
                                        .background(cyanColor)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(12.dp)
                                        .fillMaxHeight(supplyValues[index])
                                        .clip(
                                            RoundedCornerShape(
                                                topStart = 4.dp,
                                                topEnd = 4.dp
                                            )
                                        )
                                        .background(textGray.copy(alpha = 0.3f))
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = month,
                            color = textGray,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AdminRecentActivitySection(
    cyanColor: Color,
    textGray: Color,
    showActivity: Boolean,
    onViewAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Activity",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (showActivity) "Show Less" else "View All",
                color = cyanColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onViewAllClick() }
            )
        }

        if (showActivity) {
            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF142426))
                    .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(16.dp))
            ) {
                AdminActivityRow(
                    icon = Icons.Default.PersonAdd,
                    title = "New Consumer Added",
                    subtitle = "Commercial Sector • ID: 88291",
                    time = "2 MINS AGO",
                    iconColor = Color(0xFF4CAF50),
                    textGray = textGray
                )
                HorizontalDivider(color = Color(0xFF1B3B3E), thickness = 1.dp)
                AdminActivityRow(
                    icon = Icons.Default.Description,
                    title = "Monthly Report Generated",
                    subtitle = "August Usage Analytics",
                    time = "45 MINS AGO",
                    iconColor = cyanColor,
                    textGray = textGray
                )
                HorizontalDivider(color = Color(0xFF1B3B3E), thickness = 1.dp)
                AdminActivityRow(
                    icon = Icons.Default.Warning,
                    title = "Demand Threshold Alert",
                    subtitle = "North Sector exceeding 95%\ncapacity",
                    time = "3 HOURS AGO",
                    iconColor = Color(0xFFFF9800),
                    textGray = textGray
                )
            }
        }
    }
}

@Composable
fun AdminActivityRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    time: String,
    iconColor: Color,
    textGray: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = textGray,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }

        Text(
            text = time,
            color = textGray,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
    }
}



@Preview(showBackground = true)
@Composable
fun AdminDashboardPreview() {
    AdminDashboardScreen(onProfileClick = {})
}