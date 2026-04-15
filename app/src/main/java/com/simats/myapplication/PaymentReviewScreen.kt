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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.simats.PowerPulse.viewmodel.RechargeViewModel

@Composable
fun PaymentReviewScreen(
    onBackClick: () -> Unit = {},
    onPayNowClick: () -> Unit = {},
    onCardOptionClick: () -> Unit = {},
    onNetBankingOptionClick: () -> Unit = {},
    viewModel: RechargeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val cardBgColor = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)
    val dividerColor = Color(0xFF1F3336)

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
                    text = "Payment Review",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.width(32.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "ORDER SUMMARY",
                color = textGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Order Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF10363C))
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "PREPAID",
                                    color = cyanColor,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "PowerPulse Plan",
                                color = textGray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = plan.planName,
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FlashOn, null, tint = cyanColor, modifier = Modifier.size(14.dp))
                                Text(text = " ${plan.units.toInt()} kWh  ", color = textGray, fontSize = 14.sp)
                                Icon(Icons.Default.CalendarToday, null, tint = cyanColor, modifier = Modifier.size(12.dp))
                                Text(text = " ${plan.validityDays} Days", color = textGray, fontSize = 14.sp)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF10363C)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.FlashOn, null, tint = cyanColor, modifier = Modifier.size(28.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = dividerColor, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Plan Price", color = textGray, fontSize = 16.sp)
                        Text(text = "₹${String.format("%.2f", subtotal)}", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Billing Details
            Column(modifier = Modifier.fillMaxWidth()) {
                BillingInfoRow("Subtotal", "₹${String.format("%.2f", subtotal)}", textGray)
                Spacer(modifier = Modifier.height(12.dp))
                BillingInfoRow("Tax (GST 5%)", "₹${String.format("%.2f", tax)}", textGray)

                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 4.dp))
                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Total Amount", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "₹${String.format("%.2f", totalAmount)}", color = cyanColor, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "PAYMENT METHODS",
                color = textGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Payment Methods Selection
            PaymentMethodItem(
                title = "UPI (GPay, PhonePe)",
                subtitle = "Pay using your preferred UPI app",
                icon = Icons.Default.AccountBalanceWallet,
                id = "UPI",
                selectedId = selectedMethod,
                onSelect = { viewModel.selectPaymentMethod(it) },
                cyanColor = cyanColor,
                cardBgColor = cardBgColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentMethodItem(
                title = "Credit / Debit Card",
                subtitle = "Visa, Mastercard, RuPay",
                icon = Icons.Default.CreditCard,
                id = "CARD",
                selectedId = selectedMethod,
                onSelect = {
                    viewModel.selectPaymentMethod(it)
                    onCardOptionClick()
                },
                cyanColor = cyanColor,
                cardBgColor = cardBgColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentMethodItem(
                title = "Net Banking",
                subtitle = "Select from top Indian banks",
                icon = Icons.Default.AccountBalance,
                id = "BANK",
                selectedId = selectedMethod,
                onSelect = {
                    viewModel.selectPaymentMethod(it)
                    onNetBankingOptionClick()
                },
                cyanColor = cyanColor,
                cardBgColor = cardBgColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lock, null, tint = textGray, modifier = Modifier.size(14.dp))
                Spacer(Modifier.width(8.dp))
                Text(text = "PCI-DSS Compliant Secure Checkout", color = textGray, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(120.dp))
        }

        // Bottom Button
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = onPayNowClick,
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
                    Text(
                        text = "Pay Now",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.Black.copy(alpha = 0.1f))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "₹${String.format("%.2f", totalAmount)}",
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Black)
                }
            }
        }
    }
}

@Composable
fun BillingInfoRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, color = color, fontSize = 16.sp)
        Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun PaymentMethodItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    id: String,
    selectedId: String,
    onSelect: (String) -> Unit,
    cyanColor: Color,
    cardBgColor: Color
) {
    val isSelected = id == selectedId

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(cardBgColor)
            .border(
                width = if (isSelected) 2.dp else 0.dp,
                color = if (isSelected) cyanColor else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onSelect(id) }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF10363C)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = cyanColor, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = Color(0xFF4FA8B4), fontSize = 12.sp)
            }

            RadioButton(
                selected = isSelected,
                onClick = null,
                colors = RadioButtonDefaults.colors(
                    selectedColor = cyanColor,
                    unselectedColor = Color(0xFF1F3336)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentReviewPreview() {
    PaymentReviewScreen()
}
