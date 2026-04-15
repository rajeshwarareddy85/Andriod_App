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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAccountDetailsClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onSavedDataClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    userEmail: String = "",
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)

    var fullName by remember { mutableStateOf("Loading...") }
    var consumerNo by remember { mutableStateOf("...") }

    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty()) {
            try {
                val response = com.simats.PowerPulse.network.ApiClient.api.getUserProfileByEmail(userEmail)
                if (response.isSuccessful && response.body()?.ok == true) {
                    val user = response.body()?.user
                    fullName = user?.fullName ?: "Unknown User"
                    consumerNo = user?.consumerNo ?: "N/A"
                }
            } catch (e: Exception) {
                fullName = "Error Loading"
            }
        }
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
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Box(
                modifier = Modifier.size(140.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .border(4.dp, Color(0xFF10363C), CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(80.dp),
                        tint = Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(cyanColor)
                        .border(2.dp, darkBgColor, CircleShape)
                        .clickable { /* Handle edit */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile",
                        tint = darkBgColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = fullName,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = textGray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Meter: #$consumerNo",
                    color = textGray,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            ProfileOptionItem(
                icon = Icons.Default.Person,
                title = "Account Details",
                onClick = onAccountDetailsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileOptionItem(
                icon = Icons.Default.Storage,
                title = "Saved Data",
                onClick = onSavedDataClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileOptionItem(
                icon = Icons.Default.Notifications,
                title = "Notification Preferences",
                onClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileOptionItem(
                icon = Icons.Default.Shield,
                title = "Privacy Policy",
                onClick = onPrivacyClick
            )

            Spacer(modifier = Modifier.height(40.dp))

            OutlinedButton(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                border = BorderStroke(1.dp, Color(0xFF3E2723)),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFEF5350)
                )
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "VERSION 2.4.0 (BUILD 12)",
                color = Color(0xFF1B3B3E),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(120.dp))
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
            selectedTab = "profile"
        )
    }
}

@Composable
fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    trailingIcon: ImageVector = Icons.Default.KeyboardArrowRight,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        color = Color(0xFF142426).copy(alpha = 0.5f),
        shape = RoundedCornerShape(12.dp)
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
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1B3B40)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = trailingIcon,
                contentDescription = null,
                tint = Color(0xFF8B9D9F),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen()
}