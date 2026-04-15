package com.simats.PowerPulse

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OnboardingScreen(
    onNextClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(0) }
    val totalPages = 2

    val cyanColor = Color(0xFF00E5FF)
    val darkBgColor = Color(0xFF0C1D20)
    val topBarBg = Color(0xFF143034)
    val textGray = Color(0xFF8B9D9F)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
            .padding(24.dp)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (currentPage == 0) {
                // Small Logo
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(topBarBg),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(20.dp)) {
                        val path = Path().apply {
                            moveTo(size.width * 0.6f, size.height * 0.1f)
                            lineTo(size.width * 0.2f, size.height * 0.55f)
                            lineTo(size.width * 0.45f, size.height * 0.55f)
                            lineTo(size.width * 0.35f, size.height * 0.9f)
                            lineTo(size.width * 0.8f, size.height * 0.45f)
                            lineTo(size.width * 0.55f, size.height * 0.45f)
                            close()
                        }
                        drawPath(
                            path = path,
                            color = cyanColor
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "PowerPulse",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Skip",
                color = if (currentPage == 0) textGray else cyanColor.copy(alpha = 0.8f),
                fontSize = 14.sp,
                fontWeight = if (currentPage == 0) FontWeight.Normal else FontWeight.Medium,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { onSkipClick() }
                    .padding(8.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            if (currentPage == 0) {
                TrackDailyUnitsContent(cyanColor)
            } else {
                PredictNextMonthContent(cyanColor)
            }
        }

        // Pagination Dots
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val dotsCount = 4
            for (i in 0 until dotsCount) {
                val isSelected = (i == currentPage)
                
                val width = if (isSelected) 24.dp else 6.dp
                val color = if (isSelected) cyanColor else Color.Gray.copy(alpha = 0.5f)
                
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                )
                if (i < dotsCount - 1) {
                    Spacer(modifier = Modifier.width(6.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Next Button
        Button(
            onClick = {
                if (currentPage < totalPages - 1) {
                    currentPage++
                } else {
                    onNextClick()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Next",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Next",
                tint = Color.Black,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun TrackDailyUnitsContent(cyanColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Center Illustration Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF4DB6AC), Color(0xFF80CBC4))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            // Placeholder representation of the illustration
            Canvas(modifier = Modifier.size(200.dp, 200.dp)) {
                
                // Scale factor to adjust the size of the drawing
                val scale = 1.2f
                
                // Background phone/device
                drawRoundRect(
                    color = Color(0xFF0D5F66),
                    topLeft = Offset(20f * scale, 60f * scale),
                    size = androidx.compose.ui.geometry.Size(80f * scale, 120f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(16f * scale)
                )

                // Foreground meter
                drawRoundRect(
                    color = Color(0xFF1B2236),
                    topLeft = Offset(60f * scale, 30f * scale),
                    size = androidx.compose.ui.geometry.Size(110f * scale, 150f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(24f * scale)
                )

                // Screen on meter
                drawRoundRect(
                    color = Color(0xFF003333),
                    topLeft = Offset(75f * scale, 50f * scale),
                    size = androidx.compose.ui.geometry.Size(80f * scale, 35f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f * scale)
                )

                // Mock text on screen
                drawRect(
                    color = cyanColor,
                    topLeft = Offset(85f * scale, 65f * scale),
                    size = androidx.compose.ui.geometry.Size(50f * scale, 8f * scale)
                )

                // Keypad keys on meter
                val startX = 80f * scale
                val startY = 105f * scale
                val spacingX = 22f * scale
                val spacingY = 22f * scale
                for (i in 0..2) {
                    for (j in 0..1) {
                        drawRoundRect(
                            color = Color(0xFF2C3550),
                            topLeft = Offset(startX + i * spacingX, startY + j * spacingY),
                            size = androidx.compose.ui.geometry.Size(16f * scale, 12f * scale),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale)
                        )
                    }
                }
                
                // Red/Green indicator dots
                drawCircle(color = Color(0xFFE53935), radius = 5f * scale, center = Offset(85f * scale, 165f * scale))
                drawCircle(color = Color(0xFF43A047), radius = 5f * scale, center = Offset(145f * scale, 165f * scale))

                // Floating Recharge Card
                drawRoundRect(
                    color = Color(0xFF263255),
                    topLeft = Offset(150f * scale, -10f * scale),
                    size = androidx.compose.ui.geometry.Size(100f * scale, 140f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(20f * scale)
                )
                drawRoundRect(
                    color = cyanColor,
                    topLeft = Offset(160f * scale, 0f * scale),
                    size = androidx.compose.ui.geometry.Size(80f * scale, 30f * scale),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f * scale)
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        // Titles
        Text(
            text = "Track Daily Units",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Enter your daily kWh to plan your next\nrecharge",
            color = Color(0xFF8B9D9F),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 24.sp
        )
    }
}

@Composable
fun PredictNextMonthContent(cyanColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        val cardBorder = Color(0xFF1E4044)
        
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.verticalGradient(
                    colors = listOf(Color(0xFF163236), Color(0xFF0F2427))
                ))
                .border(1.dp, cardBorder, RoundedCornerShape(16.dp))
                .padding(20.dp)
        ) {
            Column {
                Text(
                    text = "USAGE FORECAST",
                    color = Color(0xFF00BFA5),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "124",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "kWh",
                        color = Color(0xFF8B9D9F),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "↗ 12%",
                        color = cyanColor,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                
                // The graph
                Spacer(modifier = Modifier.height(24.dp))
                Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val solidPath = Path().apply {
                            moveTo(0f, size.height * 0.8f)
                            cubicTo(
                                size.width * 0.15f, size.height * 0.8f,
                                size.width * 0.15f, size.height * 0.2f,
                                size.width * 0.25f, size.height * 0.2f
                            )
                            cubicTo(
                                size.width * 0.4f, size.height * 0.2f,
                                size.width * 0.4f, size.height * 0.7f,
                                size.width * 0.5f, size.height * 0.7f
                            )
                            cubicTo(
                                size.width * 0.65f, size.height * 0.7f,
                                size.width * 0.65f, size.height * 0.1f,
                                size.width * 0.75f, size.height * 0.1f
                            )
                            cubicTo(
                                size.width * 0.85f, size.height * 0.1f,
                                size.width * 0.85f, size.height * 0.85f,
                                size.width * 0.9f, size.height * 0.9f
                            )
                        }
                        
                        val dashedPath = Path().apply {
                            moveTo(size.width * 0.9f, size.height * 0.9f)
                            cubicTo(
                                size.width * 0.95f, size.height * 0.9f,
                                size.width * 0.92f, size.height * 0.2f,
                                size.width, size.height * 0.1f
                            )
                        }
                        
                        // Draw gradient under solid path
                        val fillPath = Path().apply {
                            addPath(solidPath)
                            lineTo(size.width * 0.9f, size.height)
                            lineTo(0f, size.height)
                            close()
                        }
                        
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    cyanColor.copy(alpha = 0.3f),
                                    cyanColor.copy(alpha = 0.0f)
                                )
                            )
                        )

                        // Draw solid line
                        drawPath(
                            path = solidPath,
                            color = cyanColor,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        )
                        
                        // Draw dashed line
                        drawPath(
                            path = dashedPath,
                            color = cyanColor,
                            style = Stroke(
                                width = 3.dp.toPx(),
                                cap = StrokeCap.Round,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                            )
                        )
                        
                        // Draw end point dot
                        drawCircle(
                            color = cyanColor,
                            radius = 4.dp.toPx(),
                            center = Offset(size.width * 0.9f, size.height * 0.9f)
                        )
                    }
                }
                
                // X-axis labels
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "DAY 1", color = Color(0xFF4A6B6E), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "HISTORY", color = Color(0xFF4A6B6E), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(text = "PREDICTED", color = cyanColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Titles
        Text(
            text = "Predict Next Month",
            color = Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Get forecast based on last 10 days\nusage to help you manage your\nelectricity budget better.",
            color = Color(0xFF8B9D9F),
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            lineHeight = 24.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    OnboardingScreen()
}
