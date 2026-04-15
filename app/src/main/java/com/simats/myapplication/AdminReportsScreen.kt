package com.simats.myapplication

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AdminReportsScreen(
    onBackClick: () -> Unit = {},
    onOverviewClick: () -> Unit = {},
    onSectorsClick: () -> Unit = {},
    onConsumersClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    val cardBg = Color(0xFF142426)
    val borderStroke = Color(0xFF1B3B3E)

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
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Reports & Exports",
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Distributor Analytics",
                        color = textGray,
                        fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B3B3E).copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = textGray,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Generate Report Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardBg,
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, borderStroke)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(cyanColor.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Assessment, contentDescription = null, tint = cyanColor, modifier = Modifier.size(20.dp))
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Generate Report",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        ReportInputField("FROM", "10/01/2023", Modifier.weight(1f), textGray)
                        ReportInputField("TO", "10/31/2023", Modifier.weight(1f), textGray)
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    ReportInputField("CATEGORY", "Energy Demand Analysis", Modifier.fillMaxWidth(), textGray)

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Default.FileUpload, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Generate Report", color = Color.Black, fontWeight = FontWeight.Black)
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Recent Reports Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "RECENT REPORTS",
                    color = textGray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "CLEAR HISTORY",
                    color = cyanColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            val reports = listOf(
                ReportItem("Revenue_Q3_Summary", "Oct 24, 2023 • 2.4 MB", Icons.Default.Description, Color(0xFFF44336)),
                ReportItem("Demand_Forecast_Nov", "Oct 20, 2023 • 840 KB", Icons.Default.InsertDriveFile, Color(0xFF4CAF50)),
                ReportItem("Consumer_Growth_Master", "Oct 15, 2023 • 1.2 MB", Icons.Default.InsertDriveFile, Color(0xFF2196F3)),
                ReportItem("Grid_Load_Weekly_Report", "Oct 08, 2023 • 4.1 MB", Icons.Default.Description, Color(0xFFFF9800))
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(reports) { item ->
                    ReportListRow(item, textGray, borderStroke)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Export All Button
            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, cyanColor.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFF1B3B3E).copy(alpha = 0.2f))
            ) {
                Icon(Icons.Default.FileDownload, contentDescription = null, tint = cyanColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Export All Historical Data", color = cyanColor, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        AdminBottomNavBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onOverviewClick = onOverviewClick,
            onSectorsClick = onSectorsClick,
            onConsumersClick = onConsumersClick,
            onReportsClick = { },
            selectedTab = "Reports"
        )
    }
}

@Composable
fun ReportInputField(label: String, value: String, modifier: Modifier, textGray: Color) {
    Column(modifier = modifier) {
        Text(text = label, color = textGray, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 0.5.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0C1D20))
                .border(1.dp, Color(0xFF1B3B3E), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = value, color = Color.White, fontSize = 14.sp)
        }
    }
}

data class ReportItem(val title: String, val info: String, val icon: ImageVector, val iconColor: Color)

@Composable
fun ReportListRow(item: ReportItem, textGray: Color, borderStroke: Color) {
    Surface(
        color = Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = item.icon, contentDescription = null, tint = item.iconColor, modifier = Modifier.size(22.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = item.title, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    Text(text = item.info, color = textGray, fontSize = 12.sp)
                }
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1B3B3E).copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.FileDownload, contentDescription = "Download", tint = textGray, modifier = Modifier.size(20.dp))
                }
            }
            HorizontalDivider(color = borderStroke.copy(alpha = 0.5f), thickness = 0.5.dp)
        }
    }
}

@Preview
@Composable
fun AdminReportsPreview() {
    AdminReportsScreen()
}
