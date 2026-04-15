package com.simats.myapplication

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cyanColor = Color(0xFF00E5FF)
    val darkBgColors = listOf(
        Color(0xFF132F34),
        Color(0xFF0A1B1E),
        Color(0xFF071214)
    )

    var progress by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(Unit) {
        for (i in 1..100) {
            progress = i / 100f
            delay(30)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable { onNavigateNext() }
            .background(Brush.verticalGradient(darkBgColors)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // Logo Box
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(160.dp)
            ) {
                // Glow effect and concentric circles
                Canvas(modifier = Modifier.matchParentSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                cyanColor.copy(alpha = 0.15f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.width / 2
                        ),
                        radius = size.width / 2
                    )
                    
                    drawCircle(
                        color = cyanColor.copy(alpha = 0.2f),
                        radius = size.width / 2.2f,
                        style = Stroke(width = 1.dp.toPx())
                    )
                    
                    drawCircle(
                        color = cyanColor.copy(alpha = 0.3f),
                        radius = size.width / 3.0f,
                        style = Stroke(width = 1.dp.toPx())
                    )
                }

                // Custom Lightning Bolt Icon
                Canvas(modifier = Modifier.size(48.dp)) {
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
                        color = cyanColor.copy(alpha = 0.6f),
                        style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                    )
                    drawPath(
                        path = path,
                        color = cyanColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title
            Text(
                text = "PowerPulse",
                color = Color.White,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Smart Prepaid Energy Planner",
                color = cyanColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            // Progress Bar
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                LinearProgressIndicator(
                    progress = progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = cyanColor,
                    trackColor = Color(0xFF1B4149)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "INITIALIZING INSIGHTS...",
                    color = Color.Gray,
                    fontSize = 10.sp,
                    letterSpacing = 1.5.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Bottom Text
            Text(
                text = "POWERED BY AI FORECASTING",
                color = Color.DarkGray,
                fontSize = 10.sp,
                letterSpacing = 2.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}
