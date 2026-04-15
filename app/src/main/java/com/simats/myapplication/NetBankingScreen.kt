package com.simats.PowerPulse

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NetBankingScreen(
    onBackClick: () -> Unit = {},
    onProceedToPayClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    val searchBgColor = Color(0xFF0E2225)

    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = cyanColor,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Net Banking",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp)),
                placeholder = {
                    Text(
                        "Search for your bank",
                        color = textGray.copy(alpha = 0.5f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(20.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = searchBgColor,
                    unfocusedContainerColor = searchBgColor,
                    disabledContainerColor = searchBgColor,
                    focusedBorderColor = Color(0xFF1E3A3E),
                    unfocusedBorderColor = Color(0xFF1E3A3E),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = cyanColor,
                    focusedPlaceholderColor = textGray.copy(alpha = 0.5f),
                    unfocusedPlaceholderColor = textGray.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "POPULAR BANKS",
                color = cyanColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                PopularBankCard("SBI", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                PopularBankCard("HDFC", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                PopularBankCard("ICICI", modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                PopularBankCard("Axis", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "OTHER BANKS",
                color = cyanColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                OtherBankItem("Bank of Baroda")
                Spacer(modifier = Modifier.height(12.dp))
                OtherBankItem("Canara Bank")
                Spacer(modifier = Modifier.height(12.dp))
                OtherBankItem("Kotak Mahindra Bank")
                Spacer(modifier = Modifier.height(12.dp))
                OtherBankItem("Union Bank of India")
                Spacer(modifier = Modifier.height(120.dp))
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Button(
                    onClick = onProceedToPayClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Proceed to Pay",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFF0C1D20).copy(alpha = 0.95f))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    NetBankingBottomNavItem(
                        icon = Icons.Default.Home,
                        label = "Home",
                        isSelected = false,
                        onClick = onHomeClick
                    )

                    NetBankingBottomNavItem(
                        icon = Icons.Default.BarChart,
                        label = "Usage",
                        isSelected = false,
                        onClick = onUsageClick
                    )

                    NetBankingBottomNavItem(
                        icon = Icons.Default.FlashOn,
                        label = "Plans",
                        isSelected = true,
                        onClick = onPlansClick
                    )

                    NetBankingBottomNavItem(
                        icon = Icons.Default.Person,
                        label = "Profile",
                        isSelected = false,
                        onClick = onProfileClick
                    )
                }
            }
        }
    }
}

@Composable
fun PopularBankCard(
    name: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp)
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF142426)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A3E))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = Color(0xFF0D3238),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF184952))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun OtherBankItem(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(72.dp)
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF142426)),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF1E3A3E))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0D3238)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AccountBalance,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF326E77),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun NetBankingBottomNavItem(
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
            .padding(8.dp)
            .clickable { onClick() }
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
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NetBankingPreview() {
    NetBankingScreen()
}