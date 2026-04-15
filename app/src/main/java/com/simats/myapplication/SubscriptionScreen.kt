package com.simats.myapplication

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simats.myapplication.viewmodel.SubscriptionViewModel
import com.android.billingclient.api.BillingClient

@Composable
fun SubscriptionScreen(
    onSkipClick: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SubscriptionViewModel = viewModel()
) {
    val context = LocalContext.current
    val productDetails by viewModel.productDetails.collectAsState()
    val isBillingReady by viewModel.isBillingReady.collectAsState()
    val purchaseStatus by viewModel.purchaseStatus.collectAsState()

    LaunchedEffect(purchaseStatus) {
        when (purchaseStatus) {
            "SUCCESS" -> {
                Toast.makeText(context, "Subscription successful! Welcome to Premium!", Toast.LENGTH_LONG).show()
                onSuccess()
            }
            "ALREADY_OWNED" -> {
                Toast.makeText(context, "You already have an active subscription", Toast.LENGTH_SHORT).show()
                onSuccess()
            }
            "CANCELED" -> {
                Toast.makeText(context, "Purchase canceled", Toast.LENGTH_SHORT).show()
            }
            else -> {
                if (purchaseStatus?.startsWith("ERROR") == true) {
                    Toast.makeText(context, purchaseStatus, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val darkBg = Color(0xFF0A0E27)
    val cardBg = Color(0xFF1A1F3A)
    val purpleAccent = Color(0xFF6C5CE7)
    val gold = Color(0xFFFFD700)
    val textGray = Color(0xFF7C8AA8)
    val textLight = Color(0xFFB8C5D6)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBg)
    ) {
        // Gradient overlay
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(purpleAccent.copy(alpha = 0.3f), Color.Transparent)
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Logo Card
            Card(
                modifier = Modifier.size(120.dp),
                shape = RoundedCornerShape(32.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.powerpulse_logo_512x512), // ✅ Fixed logo resource
                        contentDescription = "App Logo",
                        modifier = Modifier
                            .size(70.dp)
                            .align(Alignment.Center)
                    )
                    Text(
                        text = "✨",
                        fontSize = 28.sp,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(end = 4.dp, top = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "PowerPulse",
                color = Color.White,
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = gold.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, gold.copy(alpha = 0.3f)),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "PREMIUM",
                    color = gold,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Unlock unlimited potential with premium features designed for your success",
                color = textLight,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Features
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                FeatureItem(
                    icon = "⚡",
                    title = "Ad-Free Experience",
                    subtitle = "Pure learning, no interruptions"
                )
                Spacer(modifier = Modifier.height(12.dp))
                FeatureItem(
                    icon = "💎",
                    title = "Exclusive Tools",
                    subtitle = "Advanced analytics & insights"
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Price Card (Placeholder as per original XML)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(80.dp),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                colors = CardDefaults.cardColors(containerColor = purpleAccent)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = productDetails?.let { 
                            if (it.productType == BillingClient.ProductType.SUBS) {
                                it.subscriptionOfferDetails?.firstOrNull()?.pricingPhases?.pricingPhaseList?.firstOrNull()?.formattedPrice ?: "Premium Plan"
                            } else {
                                it.oneTimePurchaseOfferDetails?.formattedPrice ?: "Premium Plan"
                            }
                        } ?: "Get Premium Access",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Subscribe Button
            Button(
                onClick = {
                    if (isBillingReady) {
                        viewModel.launchSubscriptionFlow(context as Activity)
                    } else {
                        Toast.makeText(context, "Billing service not ready", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(32.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Start Premium",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "By continuing, you agree to our Terms & Privacy Policy",
                color = textGray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Skip Button
            TextButton(
                onClick = onSkipClick,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Maybe later",
                    color = textGray,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun FeatureItem(icon: String, title: String, subtitle: String) {
    val cardBg = Color(0xFF1A1F3A)
    val textGray = Color(0xFF7C8AA8)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 28.sp,
                modifier = Modifier.size(48.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = textGray,
                    fontSize = 14.sp
                )
            }
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color(0xFF4CAF50),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
