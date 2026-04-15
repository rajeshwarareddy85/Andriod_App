package com.simats.PowerPulse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectPlanDurationScreen(
    onBackClick: () -> Unit = {},
    onPlanSelect: (Int) -> Unit = {},
    onPredictionClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onBackClick() }
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Select Plan Duration",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF10363C).copy(alpha = 0.5f))
                    .padding(4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(cyanColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "RECOMMENDED FOR YOU",
                        color = Color.Black,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { onPredictionClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "BASED ON USAGE\nPREDICTION",
                        color = textGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            DurationPlanCard(
                title = "1 Month Plan",
                subtitle = "Standard monthly consumption",
                price = "₹1,500",
                units = "200",
                validity = "30",
                rate = "7.5",
                cyanColor = cyanColor,
                textGray = textGray,
                onBuyClick = { onPlanSelect(1) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            DurationPlanCard(
                title = "2 Months Plan",
                subtitle = "Bi-monthly convenience",
                price = "₹2,850",
                oldPrice = "₹3,000",
                units = "400",
                validity = "60",
                rate = "7.1",
                badgeText = "SAVE 5%",
                isFeatured = true,
                cyanColor = cyanColor,
                textGray = textGray,
                onBuyClick = { onPlanSelect(2) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            DurationPlanCard(
                title = "3 Months Plan",
                subtitle = "Quarterly saver pack",
                price = "₹4,050",
                oldPrice = "₹4,500",
                units = "600",
                validity = "90",
                rate = "6.7",
                badgeText = "BEST VALUE • SAVE 10%",
                badgeColor = Color(0xFFFFD600),
                cyanColor = cyanColor,
                textGray = textGray,
                onBuyClick = { onPlanSelect(3) }
            )

            Spacer(modifier = Modifier.height(100.dp))
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
            selectedTab = "plans"
        )
    }
}

@Composable
fun DurationPlanCard(
    title: String,
    subtitle: String,
    price: String,
    oldPrice: String? = null,
    units: String,
    validity: String,
    rate: String,
    badgeText: String? = null,
    badgeColor: Color = Color(0xFF00E5FF),
    isFeatured: Boolean = false,
    cyanColor: Color,
    textGray: Color,
    onBuyClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                color = if (isFeatured) cyanColor else Color(0xFF1F3336),
                shape = RoundedCornerShape(24.dp)
            )
            .background(Color(0xFF142426))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = subtitle,
                        color = textGray,
                        fontSize = 14.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(horizontalAlignment = Alignment.End) {
                    if (badgeText != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(badgeColor)
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = badgeText,
                                color = Color.Black,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Text(
                        text = price,
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if (oldPrice != null) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = oldPrice,
                            color = textGray,
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Divider(
                color = Color(0xFF1F3336),
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PlanDetailItem(
                    label = "UNITS",
                    value = units,
                    unit = "kWh",
                    modifier = Modifier.weight(1f)
                )
                PlanDetailItem(
                    label = "VALIDITY",
                    value = validity,
                    unit = "Days",
                    modifier = Modifier.weight(1f)
                )
                PlanDetailItem(
                    label = "RATE",
                    value = "₹$rate",
                    unit = "/u",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onBuyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFeatured) cyanColor else Color(0xFF1F2937).copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "BUY THIS PLAN",
                    color = if (isFeatured) Color.Black else cyanColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun PlanDetailItem(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            color = Color(0xFF8B9D9F),
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = value,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = unit,
            color = Color(0xFF8B9D9F),
            fontSize = 12.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SelectPlanDurationPreview() {
    SelectPlanDurationScreen()
}