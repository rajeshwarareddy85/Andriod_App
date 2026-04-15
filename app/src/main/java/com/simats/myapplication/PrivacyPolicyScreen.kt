package com.simats.PowerPulse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
fun PrivacyPolicyScreen(
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Privacy Policy",
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
                .padding(20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            PrivacySection(
                icon = Icons.Default.Person,
                title = "Data Collection",
                content = "We collect information to provide better services to all our users. This includes energy consumption patterns, device identifiers, and basic profile information used to optimize your home's energy efficiency. We do not sell your personal data to third parties.",
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(40.dp))

            PrivacySection(
                icon = Icons.Default.FlashOn,
                title = "Energy Usage Privacy",
                content = "Your real-time energy usage data is encrypted during transmission and stored securely. This data is only accessible by you and authorized automated systems for providing efficiency insights. Usage patterns are anonymized for regional grid balancing research.",
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Highlighted Quote Box
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                color = cardBg.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "\"Our goal is to maximize your savings while maintaining the highest standard of data integrity.\"",
                    color = textGray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(20.dp),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            PrivacySection(
                icon = Icons.Default.Lock,
                title = "Security Measures",
                content = "We employ industry-standard AES-256 encryption for all stored data. Regular security audits are performed to ensure your smart home configuration remains protected from unauthorized access. Multi-factor authentication is recommended for all accounts.",
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Updates Footer
            Text(
                text = "UPDATES",
                color = cyanColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Last updated: October 24, 2023. We may update this policy periodically to reflect changes in our practices or service offerings.",
                color = textGray,
                fontSize = 12.sp,
                lineHeight = 18.sp
            )
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun PrivacySection(
    icon: ImageVector,
    title: String,
    content: String,
    cyanColor: Color,
    textGray: Color
) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
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
            Text(
                text = title,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = content,
            color = textGray,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    }
}
