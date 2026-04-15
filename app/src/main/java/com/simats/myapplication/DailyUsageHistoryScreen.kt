package com.simats.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.DailyUsageItem
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyUsageHistoryScreen(
    userEmail: String,
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

    var historyList by remember { mutableStateOf<List<DailyUsageItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userEmail) {
        scope.launch {
            try {
                // 1. Get user profile to find consumerNo
                val profileRes = ApiClient.api.getUserProfileByEmail(userEmail)
                if (profileRes.isSuccessful && profileRes.body()?.ok == true) {
                    val cNo = profileRes.body()?.user?.consumerNo
                    if (cNo != null) {
                        // 2. Fetch history
                        val historyRes = ApiClient.api.getDailyUsageHistory(cNo)
                        if (historyRes.isSuccessful && historyRes.body()?.ok == true) {
                            historyList = historyRes.body()?.history ?: emptyList()
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

    // Filter and group
    val filteredHistory = historyList.filter { 
        it.usageDate?.contains(searchQuery, ignoreCase = true) == true ||
        searchQuery.isEmpty()
    }.sortedByDescending { it.usageDate }

    // Logic to group by month
    val grouped = filteredHistory.groupBy { item ->
        try {
            val date = LocalDate.parse(item.usageDate)
            val monthYear = date.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)).uppercase()
            monthYear
        } catch (e: Exception) {
            "UNKNOWN"
        }
    }

    Scaffold(
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
                selectedTab = "usage" // As per user screenshot
            )
        },
        containerColor = darkBgColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(50.dp))

            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Text(
                    text = "Daily Usage History",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(48.dp)) // To center title
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF142426),
                    unfocusedContainerColor = Color(0xFF142426),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = cyanColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                placeholder = { Text("Search history", color = Color(0xFF435A5C)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF435A5C)) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                FilterChip(
                    label = "This Week",
                    onClick = { /* Implement filter */ },
                    selectedColor = cyanColor,
                    darkBg = Color(0xFF0F3238)
                )
                FilterChip(
                    label = "This Month",
                    onClick = { /* Implement filter */ },
                    selectedColor = Color(0xFF1E2D2F),
                    darkBg = Color(0xFF1E2D2F)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = cyanColor)
                }
            } else if (historyList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No usage records found.", color = textGray)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    grouped.forEach { (month, items) ->
                        item {
                            Text(
                                text = month,
                                color = textGray,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        items(items) { record ->
                            UsageHistoryCard(
                                record = record,
                                cyanColor = cyanColor,
                                cardBg = cardBg,
                                textGray = textGray
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChip(
    label: String,
    onClick: () -> Unit,
    selectedColor: Color,
    darkBg: Color
) {
    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() },
        color = darkBg
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = if (selectedColor == Color(0xFF00E5FF)) Color(0xFF00E5FF) else Color(0xFF8B9D9F), fontSize = 14.sp)
            if (label.contains("Week") || label.contains("Month")) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = if (selectedColor == Color(0xFF00E5FF)) Color(0xFF00E5FF) else Color(0xFF8B9D9F), modifier = Modifier.size(16.dp))
            }
        }
    }
}

@Composable
fun UsageHistoryCard(
    record: DailyUsageItem,
    cyanColor: Color,
    cardBg: Color,
    textGray: Color
) {
    val dateObj = try { LocalDate.parse(record.usageDate) } catch (e: Exception) { null }
    val formattedDate = dateObj?.format(DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.US)) ?: record.usageDate ?: "N/A"
    val dayName = dateObj?.format(DateTimeFormatter.ofPattern("EEEE", Locale.US)) ?: ""

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = cardBg,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0F3238)),
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

            // Date and Day
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formattedDate,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = dayName,
                    color = textGray,
                    fontSize = 14.sp
                )
            }

            // Usage
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${record.usageKwh ?: 0.0} kWh",
                    color = cyanColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "CONSUMPTION",
                    color = textGray,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
