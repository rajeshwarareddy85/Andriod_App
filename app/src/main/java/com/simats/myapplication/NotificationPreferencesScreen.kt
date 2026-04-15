package com.simats.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationPreferencesScreen(
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val cardBg = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)

    var usageAlerts by remember { mutableStateOf(false) }
    var rechargeReminders by remember { mutableStateOf(true) }
    var priceUpdates by remember { mutableStateOf(false) }
    var weeklyReports by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Notification Preferences",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = darkBgColor),
                modifier = Modifier.padding(top = 16.dp)
            )
        },
        bottomBar = {
            ProfileBottomNavBar(
                cyanColor = cyanColor,
                textGray = textGray,
                darkBgColor = darkBgColor,
                onHomeClick = onHomeClick,
                onUsageClick = onUsageClick,
                onPlansClick = onPlansClick,
                onProfileClick = onProfileClick,
                onAIClick = onAIClick,
                selectedTab = "profile"
            )
        },
        containerColor = darkBgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            NotificationToggleRow(
                icon = Icons.Default.AccessTime,
                title = "Daily Usage Alerts",
                subtitle = "Get notified about your daily consumption",
                checked = usageAlerts,
                onCheckedChange = { usageAlerts = it },
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            NotificationToggleRow(
                icon = Icons.Default.CreditCard,
                title = "Recharge Reminders",
                subtitle = "Never run out of balance with alerts",
                checked = rechargeReminders,
                onCheckedChange = { rechargeReminders = it },
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            NotificationToggleRow(
                icon = Icons.Default.List,
                title = "Price Updates",
                subtitle = "Stay informed about new plan pricing",
                checked = priceUpdates,
                onCheckedChange = { priceUpdates = it },
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            NotificationToggleRow(
                icon = Icons.Default.BarChart,
                title = "Weekly Reports",
                subtitle = "Summary of your activity every Monday",
                checked = weeklyReports,
                onCheckedChange = { weeklyReports = it },
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Pro Tip Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF10363C), RoundedCornerShape(16.dp)),
                color = cardBg.copy(alpha = 0.5f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Pro Tip",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Enabling \"Daily Usage Alerts\" helps 80% of our users save up to 15% on their monthly bills.",
                            color = textGray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationToggleRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    cyanColor: Color,
    textGray: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF143034)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = cyanColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = subtitle,
                color = textGray,
                fontSize = 14.sp
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF37474F), // Darker track as per mockup
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF263238)
            )
        )
    }
}
