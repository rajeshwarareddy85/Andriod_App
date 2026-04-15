package com.simats.PowerPulse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumerManagementScreen(
    onBackClick: () -> Unit = {},
    onOverviewClick: () -> Unit = {},
    onSectorsClick: () -> Unit = {},
    onConsumersClick: () -> Unit = {},
    onReportsClick: () -> Unit = {},
    onDetailClick: (String) -> Unit = {},
    viewModel: com.simats.PowerPulse.viewmodel.AdminConsumerManagementViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    val cardBg = Color(0xFF142426)
    val borderStroke = Color(0xFF1B3B3E)

    var searchQuery by remember { mutableStateOf("") }
    val filters = listOf("All Consumers", "High Usage", "Priority")
    var selectedFilter by remember { mutableStateOf("All Consumers") }

    val consumerState by viewModel.consumerState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchConsumers()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
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
                    text = "Consumer Management",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = "Filters",
                    tint = cyanColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search by ID or Mandal...", color = textGray.copy(alpha = 0.5f), fontSize = 14.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = textGray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                shape = RoundedCornerShape(12.dp),
                colors = adminTextFieldColors(Color(0xFF112224), cyanColor)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Filter Pills
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                filters.forEach { filter ->
                    FilterPill(
                        label = filter,
                        isSelected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        cyanColor = cyanColor,
                        textGray = textGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = consumerState) {
                is com.simats.PowerPulse.viewmodel.ConsumerManagementState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = cyanColor)
                    }
                }
                is com.simats.PowerPulse.viewmodel.ConsumerManagementState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = "Error: ${state.message}", color = Color.Red, fontSize = 14.sp)
                    }
                }
                is com.simats.PowerPulse.viewmodel.ConsumerManagementState.Success -> {
                    val filteredConsumers = state.data.consumers.filter { consumer ->
                        val matchesSearch = consumer.consumerNo.contains(searchQuery, ignoreCase = true) || 
                                           consumer.fullName.contains(searchQuery, ignoreCase = true) ||
                                           consumer.mandal.contains(searchQuery, ignoreCase = true)
                        val matchesFilter = when (selectedFilter) {
                            "High Usage" -> consumer.status == "HIGH USAGE"
                            "Priority" -> consumer.status == "PRIORITY"
                            else -> true
                        }
                        matchesSearch && matchesFilter
                    }

                    if (filteredConsumers.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No consumers found", color = textGray, fontSize = 14.sp)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(20.dp),
                            contentPadding = PaddingValues(bottom = 120.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(filteredConsumers) { consumer ->
                                ConsumerCard(
                                    info = consumer,
                                    cyanColor = cyanColor,
                                    textGray = textGray,
                                    cardBg = cardBg,
                                    borderStroke = borderStroke,
                                    onViewDetails = { onDetailClick(consumer.consumerNo) }
                                )
                            }
                        }
                    }
                }
            }
        }

        // Floating Action Button
        FloatingActionButton(
            onClick = { /* Add consumer */ },
            containerColor = cyanColor,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 24.dp, bottom = 100.dp)
                .size(60.dp)
        ) {
            Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.Black, modifier = Modifier.size(32.dp))
        }

        AdminBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onOverviewClick = onOverviewClick,
            onSectorsClick = onSectorsClick,
            onConsumersClick = onConsumersClick,
            onReportsClick = onReportsClick,
            selectedTab = "Consumers"
        )
    }
}

@Composable
fun FilterPill(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    cyanColor: Color,
    textGray: Color
) {
    Surface(
        color = if (isSelected) cyanColor else Color(0xFF1B3B3E).copy(alpha = 0.3f),
        shape = RoundedCornerShape(20.dp),
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1B3B3E)) else null,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else textGray,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
        )
    }
}

@Composable
fun ConsumerCard(
    info: com.simats.PowerPulse.model.ConsumerData,
    cyanColor: Color,
    textGray: Color,
    cardBg: Color,
    borderStroke: Color,
    onViewDetails: () -> Unit = {}
) {
    val badgeColor = when (info.status) {
        "PRIORITY" -> Color(0xFF00E5FF)
        "HIGH USAGE" -> Color(0xFFFF9800)
        else -> textGray
    }
    
    val trendIcon = if (info.avgUnits30D < info.predictedNextMonth / 30.0) Icons.Default.TrendingUp else Icons.Default.TrendingDown

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBg)
            .border(1.dp, borderStroke, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF1B3B3E).copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = cyanColor, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = info.consumerNo, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = info.mandal, color = textGray, fontSize = 12.sp)
            }
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                color = badgeColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = info.status,
                    color = badgeColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Divider(color = borderStroke, thickness = 0.5.dp)
        Spacer(modifier = Modifier.height(20.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "AVG UNITS (30D)", color = textGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Text(text = "${info.avgUnits30D} units", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "PREDICTED NEXT", color = textGray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${info.predictedNextMonth} units", color = badgeColor, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(imageVector = trendIcon, contentDescription = null, tint = badgeColor, modifier = Modifier.size(16.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onViewDetails,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = if(info.status == "HIGH USAGE" || info.status == "PRIORITY") cyanColor else Color(0xFF112224)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "View Details",
                    color = if(info.status == "HIGH USAGE" || info.status == "PRIORITY") Color.Black else cyanColor,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = if(info.status == "HIGH USAGE" || info.status == "PRIORITY") Color.Black else cyanColor,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
