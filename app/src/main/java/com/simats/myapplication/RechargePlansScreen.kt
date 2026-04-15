package com.simats.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.RechargePlan
import com.simats.myapplication.model.UsagePredictPlan
import com.simats.myapplication.viewmodel.RechargeViewModel

@Composable
fun RechargePlansScreen(
    onBackClick: () -> Unit = {},
    onBuyClick: () -> Unit = {},
    onRecommendedClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onUsageClick: () -> Unit = {},
    onPlansClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onAIClick: () -> Unit = {},
    viewModel: RechargeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val cardBgColor = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)

    val plans by viewModel.plans.collectAsState()
    val usagePredictPlans by viewModel.usagePredictPlans.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchPlans()
    }

    val activePlans = if (selectedTab == "RECOMMENDED") plans else emptyList()
    val activeUsagePlans = if (selectedTab == "USAGE_PREDICT") usagePredictPlans else emptyList()

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

            // Header Section
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
                    text = "Select Recharge Plan",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = cyanColor)
                }
            } else if (error != null) {
                Text(text = error ?: "Error loading plans", color = Color.Red, modifier = Modifier.padding(16.dp))
            } else {
                // Recommended Tag Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RECOMMENDED FOR YOU",
                        color = if (selectedTab == "RECOMMENDED") cyanColor else textGray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.clickable { viewModel.selectTab("RECOMMENDED") }
                    )
                    
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedTab == "USAGE_PREDICT") Color(0xFF10363C) else Color.Transparent)
                            .clickable { viewModel.selectTab("USAGE_PREDICT") }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "USAGE PREDICTION PLANS",
                            color = if (selectedTab == "USAGE_PREDICT") cyanColor else textGray,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedTab == "RECOMMENDED") {
                    // Featured Recommended Card (Take the first plan as featured for now)
                    activePlans.firstOrNull()?.let { featuredPlan ->
                        FeaturedPlanCard(cyanColor, featuredPlan) {
                            viewModel.selectPlan(featuredPlan)
                            onBuyClick()
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // All Other Plans Section
                    if (activePlans.size > 1) {
                        Text(
                            text = "ALL OTHER PLANS",
                            color = textGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Other Plans List
                        activePlans.drop(1).forEach { plan ->
                            OtherPlanItem(
                                plan = plan,
                                cyanColor = cyanColor,
                                onSelect = {
                                    viewModel.selectPlan(plan)
                                    onBuyClick()
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                } else if (selectedTab == "USAGE_PREDICT") {
                    if (activeUsagePlans.isEmpty()) {
                        Text("No Usage Prediction Plans found.", color = textGray)
                    } else {
                        // All Usage Predict Plans Section
                        Text(
                            text = "PREDICTED FOR YOUR USAGE",
                            color = textGray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        activeUsagePlans.forEach { plan ->
                            OtherUsagePlanItem(
                                plan = plan,
                                cyanColor = cyanColor,
                                onSelect = {
                                    viewModel.selectUsagePredictPlan(plan)
                                    onBuyClick()
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

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
            selectedTab = "plans"
        )
    }
}

@Composable
fun FeaturedPlanCard(cyanColor: Color, plan: RechargePlan, onBuyClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFF1F3336), RoundedCornerShape(24.dp))
            .background(Color(0xFF142426))
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = plan.planName,
                        color = cyanColor,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = plan.description ?: "Perfect for your needs",
                        color = Color(0xFF8B9D9F),
                        fontSize = 14.sp
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
                            .background(cyanColor)
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                            .offset(x = 24.dp, y = (-24).dp)
                    ) {
                        Text(
                            text = "BEST VALUE",
                            color = Color.Black,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Text(
                        text = "₹${plan.amount.toInt()}",
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            HorizontalDivider(color = Color(0xFF1F3336), thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                PlanDetailItem("UNITS", "${plan.units.toInt()}", "kWh")
                PlanDetailItem("VALIDITY", "${plan.validityDays}", "Days")
                PlanDetailItem("RATE", "₹${String.format("%.1f", plan.amount / plan.units)}", "/u")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onBuyClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
                shape = RoundedCornerShape(12.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = "BUY THIS PLAN",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun PlanDetailItem(label: String, value: String, unit: String) {
    Column {
        Text(text = label, color = Color(0xFF4FA8B4), fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Row(verticalAlignment = Alignment.Bottom) {
            Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = unit, color = Color(0xFF8B9D9F), fontSize = 12.sp, modifier = Modifier.padding(bottom = 2.dp))
        }
    }
}

@Composable
fun OtherPlanItem(plan: RechargePlan, cyanColor: Color, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF142426))
            .clickable { onSelect() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = plan.planName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${plan.units.toInt()} kWh", color = cyanColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "  •  ", color = Color(0xFF1F3336))
                    Text(text = "${plan.validityDays} Days", color = Color(0xFF8B9D9F), fontSize = 14.sp)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "₹${plan.amount.toInt()}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "SELECT",
                    color = cyanColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Composable
fun OtherUsagePlanItem(plan: UsagePredictPlan, cyanColor: Color, onSelect: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF142426))
            .clickable { onSelect() }
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = plan.planName, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${plan.units.toInt()} kWh", color = cyanColor, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text(text = "  •  ", color = Color(0xFF1F3336))
                    Text(text = "${plan.validityDays} Days", color = Color(0xFF8B9D9F), fontSize = 14.sp)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(text = "₹${plan.amount.toInt()}", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "SELECT",
                    color = cyanColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RechargePlansPreview() {
    RechargePlansScreen()
}
