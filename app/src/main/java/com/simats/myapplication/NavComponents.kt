package com.simats.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun ProfileBottomNavBar(
    modifier: Modifier = Modifier,
    cyanColor: Color,
    textGray: Color,
    darkBgColor: Color,
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    selectedTab: String = "profile"
) {
    Surface(
        color = Color(0xFF102A2D),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                isSelected = selectedTab == "home",
                onClick = onHomeClick
            )
            BottomNavItem(
                icon = Icons.Default.Add,
                label = "Usage",
                isSelected = selectedTab == "usage",
                onClick = onUsageClick
            )
            BottomNavItem(
                icon = Icons.Default.QuestionAnswer,
                label = "AI",
                isSelected = selectedTab == "ai",
                onClick = onAIClick
            )
            BottomNavItem(
                icon = Icons.Default.Menu,
                label = "Plans",
                isSelected = selectedTab == "plans",
                onClick = onPlansClick
            )
            BottomNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                isSelected = selectedTab == "profile",
                activeColor = cyanColor,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    activeColor: Color = Color.White,
    onClick: () -> Unit = {}
) {
    val textGray = Color(0xFF8B9D9F)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) activeColor else textGray,
            modifier = Modifier.size(24.dp)
        )

        Text(
            text = label,
            color = if (isSelected) activeColor else textGray,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun AdminBottomNavBar(
    modifier: Modifier = Modifier,
    onOverviewClick: () -> Unit = {},
    onSectorsClick: () -> Unit = {},
    onConsumersClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    selectedTab: String = "Overview"
) {
    val darkBgColor = Color(0xFF0C1D20)

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp),
        color = darkBgColor.copy(alpha = 0.95f),
        border = BorderStroke(width = 0.5.dp, color = Color(0xFF1B3B3E))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AdminBottomNavItem(
                icon = Icons.Default.GridView,
                label = "Overview",
                isSelected = selectedTab == "Overview",
                onClick = onOverviewClick
            )
            AdminBottomNavItem(
                icon = Icons.Default.Dashboard,
                label = "Sectors",
                isSelected = selectedTab == "Sectors",
                onClick = onSectorsClick
            )
            AdminBottomNavItem(
                icon = Icons.Default.Groups,
                label = "Consumers",
                isSelected = selectedTab == "Consumers",
                onClick = onConsumersClick
            )
            AdminBottomNavItem(
                icon = Icons.Default.BarChart,
                label = "Reports",
                isSelected = selectedTab == "Reports",
                onClick = onReportsClick
            )
        }
    }
}

@Composable
fun AdminBottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable(onClick = onClick)
            .padding(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isSelected) cyanColor else textGray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = if (isSelected) cyanColor else textGray,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
        )
    }
}

@Composable
fun AdminLabel(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun adminTextFieldColors(
    containerColor: Color,
    cyanColor: Color,
    textGray: Color = Color(0xFF8B9D9F)
) = OutlinedTextFieldDefaults.colors(
    focusedTextColor = Color.White,
    unfocusedTextColor = Color.White,
    focusedContainerColor = containerColor,
    unfocusedContainerColor = containerColor,
    focusedBorderColor = cyanColor,
    unfocusedBorderColor = Color(0xFF1E3A40),
    focusedLabelColor = cyanColor,
    unfocusedLabelColor = textGray,
    cursorColor = cyanColor,
    focusedPlaceholderColor = Color(0xFF2C4E52),
    unfocusedPlaceholderColor = Color(0xFF2C4E52)
)
