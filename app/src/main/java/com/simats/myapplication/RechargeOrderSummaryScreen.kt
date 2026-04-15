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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import com.simats.PowerPulse.viewmodel.RechargeViewModel
import java.util.*

@Composable
fun RechargeOrderSummaryScreen(
    onBackClick: () -> Unit = {},
    onSecurePayClick: () -> Unit = {},
    viewModel: RechargeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val cardBgColor = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)
    val dividerColor = Color(0xFF1F3336)
    val context = LocalContext.current

    val selectedPlan by viewModel.selectedPlan.collectAsState()
    val selectedMethod by viewModel.selectedPaymentMethod.collectAsState()

    if (selectedPlan == null) {
        Box(modifier = Modifier.fillMaxSize().background(darkBgColor), contentAlignment = Alignment.Center) {
            Text(text = "No plan selected", color = Color.White)
        }
        return
    }

    val plan = selectedPlan!!
    val subtotal = plan.amount
    val tax = subtotal * 0.05
    val totalAmount = subtotal + tax

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
                    tint = cyanColor,
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onBackClick() }
                )
                Text(
                    text = "Recharge Order Summary",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(32.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Selected Plan Section
            Text(
                text = "SELECTED PLAN",
                color = cyanColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Plan Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFF1F3336), RoundedCornerShape(20.dp))
                    .background(Color(0xFF10363C).copy(alpha = 0.5f))
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${plan.units} Units",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = plan.planName,
                                color = textGray,
                                fontSize = 16.sp
                            )
                        }
                        
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(24.dp))
                                .background(cyanColor)
                                .padding(horizontal = 20.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "₹${plan.amount.toInt()}",
                                color = Color.Black.copy(alpha = 0.8f),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = dividerColor.copy(alpha = 0.5f), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn, 
                            contentDescription = null, 
                            tint = cyanColor, 
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Estimated duration: ${plan.validityDays} days",
                            color = cyanColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Cost Breakdown Section
            Text(
                text = "COST BREAKDOWN",
                color = textGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    BreakdownRow("Plan Price", "₹${String.format("%.2f", subtotal)}", textGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    BreakdownRow("Tax (5%)", "₹${String.format("%.2f", tax)}", textGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    BreakdownRow("Payment Method", selectedMethod ?: "Not Selected", textGray)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Divider(color = dividerColor, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Amount",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "₹${String.format("%.2f", totalAmount)}",
                            color = cyanColor,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Security Footer
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PCI-DSS COMPLIANT",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Your transaction is protected with\nmilitary-grade 128-bit SSL encryption",
                    color = textGray,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Bottom Secure Pay Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = {
                    val upiUri = Uri.parse("upi://pay").buildUpon()
                        .appendQueryParameter("pa", "provider@upi") // Example VPA
                        .appendQueryParameter("pn", "PowerPulse")
                        .appendQueryParameter("mc", "")
                        .appendQueryParameter("tid", UUID.randomUUID().toString())
                        .appendQueryParameter("tr", UUID.randomUUID().toString())
                        .appendQueryParameter("tn", "Recharge for ${plan.planName}")
                        .appendQueryParameter("am", String.format("%.2f", totalAmount))
                        .appendQueryParameter("cu", "INR")
                        .build()

                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = upiUri
                    
                    // Try to direct to PhonePe specifically if installed
                    intent.setPackage("com.phonepe.app")

                    try {
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        // Fallback to any available UPI app if PhonePe is not found
                        intent.setPackage(null)
                        try {
                            context.startActivity(intent)
                        } catch (e2: Exception) {
                            Toast.makeText(context, "No UPI app found. Please install PhonePe.", Toast.LENGTH_LONG).show()
                        }
                    }
                    onSecurePayClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield, 
                        contentDescription = null, 
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Secure Pay: ₹${String.format("%.2f", totalAmount)}",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}

@Composable
fun BreakdownRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = color, fontSize = 16.sp)
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun RechargeOrderSummaryPreview() {
    RechargeOrderSummaryScreen()
}
