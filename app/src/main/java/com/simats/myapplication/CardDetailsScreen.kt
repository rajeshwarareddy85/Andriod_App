package com.simats.PowerPulse

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    onBackClick: () -> Unit = {},
    onPaySecurelyClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val textGray = Color(0xFF8B9D9F)
    val inputBgColor = Color(0xFF142426)
    val inputBorderColor = Color(0xFF1E5154)

    var cardNumber by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }
    var expiryDate by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }

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
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = "Card Details",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF0F2224))
                    .border(1.dp, Color(0xFF163C3F), RoundedCornerShape(24.dp))
                    .padding(24.dp)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "CREDIT CARD",
                                color = cyanColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .size(width = 40.dp, height = 24.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(
                                                cyanColor.copy(alpha = 0.5f),
                                                Color.Transparent
                                            )
                                        )
                                    )
                                    .border(
                                        0.5.dp,
                                        cyanColor.copy(alpha = 0.3f),
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }

                        Text(
                            text = "VISA",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = if (cardNumber.isBlank()) "XXXX XXXX XXXX 4242" else cardNumber,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "CARD HOLDER",
                                color = textGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (cardholderName.isBlank()) "JOHN DOE" else cardholderName.uppercase(),
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "EXPIRES",
                                color = textGray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (expiryDate.isBlank()) "12/28" else expiryDate,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Card Number",
                color = textGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "1234 5678 9101 1121",
                        color = textGray.copy(alpha = 0.5f)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(24.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputBgColor,
                    unfocusedContainerColor = inputBgColor,
                    disabledContainerColor = inputBgColor,
                    focusedBorderColor = cyanColor,
                    unfocusedBorderColor = inputBorderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = cyanColor,
                    focusedPlaceholderColor = textGray.copy(alpha = 0.5f),
                    unfocusedPlaceholderColor = textGray.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cardholder Name",
                color = textGray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = cardholderName,
                onValueChange = { cardholderName = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        "e.g. John Doe",
                        color = textGray.copy(alpha = 0.5f)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = inputBgColor,
                    unfocusedContainerColor = inputBgColor,
                    disabledContainerColor = inputBgColor,
                    focusedBorderColor = cyanColor,
                    unfocusedBorderColor = inputBorderColor,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    cursorColor = cyanColor,
                    focusedPlaceholderColor = textGray.copy(alpha = 0.5f),
                    unfocusedPlaceholderColor = textGray.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Expiry Date (MM/YY)",
                        color = textGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = expiryDate,
                        onValueChange = { expiryDate = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "MM/YY",
                                color = textGray.copy(alpha = 0.5f)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = inputBgColor,
                            unfocusedContainerColor = inputBgColor,
                            disabledContainerColor = inputBgColor,
                            focusedBorderColor = cyanColor,
                            unfocusedBorderColor = inputBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = cyanColor,
                            focusedPlaceholderColor = textGray.copy(alpha = 0.5f),
                            unfocusedPlaceholderColor = textGray.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "CVV",
                        color = textGray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(
                                "***",
                                color = textGray.copy(alpha = 0.5f)
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.HelpOutline,
                                contentDescription = null,
                                tint = textGray,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = inputBgColor,
                            unfocusedContainerColor = inputBgColor,
                            disabledContainerColor = inputBgColor,
                            focusedBorderColor = cyanColor,
                            unfocusedBorderColor = inputBorderColor,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = cyanColor,
                            focusedPlaceholderColor = textGray.copy(alpha = 0.5f),
                            unfocusedPlaceholderColor = textGray.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(14.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "256-bit SSL Secure Payment",
                    color = textGray,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Button(
                onClick = onPaySecurelyClick,
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
                        text = "Pay Securely",
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardDetailsPreview() {
    CardDetailsScreen()
}