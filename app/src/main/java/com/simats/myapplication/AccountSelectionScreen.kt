package com.simats.PowerPulse

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AccountSelectionScreen(
    onConsumerClick: () -> Unit = {},
    onAdminClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cyanColor = Color(0xFF00E5FF)
    val darkBgColor = Color(0xFF0C1D20)
    val cardBgColor = Color(0xFF14282B)
    val iconBgColor = Color(0xFF10363C)
    val textGray = Color(0xFF8B9D9F)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
            .padding(horizontal = 24.dp, vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo & Title Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            // Logo Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(cyanColor),
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
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "POWERPULSE",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Titles
        Text(
            text = "How would you like to\ncontinue?",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select your account type to manage\nelectricity consumption",
            color = textGray,
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Consumer Card
        AccountTypeCard(
            title = "Login as Consumer",
            subtitle = "Track home usage & monthly\npredictions",
            iconContent = {
                // User & Search icon
                Canvas(modifier = Modifier.size(24.dp)) {
                    // Head
                    drawCircle(
                        color = cyanColor,
                        radius = size.width * 0.2f,
                        center = Offset(size.width * 0.35f, size.height * 0.3f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    // Shoulders
                    val shoulderPath = Path().apply {
                        moveTo(size.width * 0.1f, size.height * 0.8f)
                        quadraticBezierTo(
                            size.width * 0.35f, size.height * 0.5f,
                            size.width * 0.6f, size.height * 0.8f
                        )
                    }
                    drawPath(
                        path = shoulderPath,
                        color = cyanColor,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    
                    // Mag Glass Search
                    drawCircle(
                        color = cyanColor,
                        radius = size.width * 0.2f,
                        center = Offset(size.width * 0.75f, size.height * 0.65f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    drawLine(
                        color = cyanColor,
                        start = Offset(size.width * 0.85f, size.height * 0.75f),
                        end = Offset(size.width, size.height * 0.9f),
                        strokeWidth = 2.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            },
            bgColor = cardBgColor,
            iconBgColor = iconBgColor,
            onClick = onConsumerClick
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Administrator Card
        AccountTypeCard(
            title = "Login as\nAdministrator",
            subtitle = "Manage utility board &\ndistributor tools",
            iconContent = {
                // Shield User icon
                Canvas(modifier = Modifier.size(24.dp)) {
                    // Shield
                    val shieldPath = Path().apply {
                        moveTo(size.width * 0.15f, size.height * 0.1f)
                        lineTo(size.width * 0.85f, size.height * 0.1f)
                        lineTo(size.width * 0.85f, size.height * 0.5f)
                        cubicTo(
                            size.width * 0.85f, size.height * 0.75f,
                            size.width * 0.5f, size.height * 0.9f,
                            size.width * 0.5f, size.height * 0.9f
                        )
                        cubicTo(
                            size.width * 0.5f, size.height * 0.9f,
                            size.width * 0.15f, size.height * 0.75f,
                            size.width * 0.15f, size.height * 0.5f
                        )
                        close()
                    }
                    drawPath(
                        path = shieldPath,
                        color = cyanColor,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                    // Inner user head
                    drawCircle(
                        color = cyanColor,
                        radius = size.width * 0.15f,
                        center = Offset(size.width * 0.5f, size.height * 0.4f),
                        style = Stroke(width = 2.dp.toPx())
                    )
                    // Inner user shoulders
                    val userPath = Path().apply {
                        moveTo(size.width * 0.3f, size.height * 0.7f)
                        quadraticBezierTo(
                            size.width * 0.5f, size.height * 0.5f,
                            size.width * 0.7f, size.height * 0.7f
                        )
                    }
                    drawPath(
                        path = userPath,
                        color = cyanColor,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            },
            bgColor = cardBgColor,
            iconBgColor = iconBgColor,
            onClick = onAdminClick
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "By continuing, you agree to our Terms of Service\nand Privacy Policy",
            color = textGray,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Divider(color = Color(0xFF1E3336), modifier = Modifier.weight(1f))
            Text(
                text = "SECURED ACCESS",
                color = textGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Divider(color = Color(0xFF1E3336), modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun AccountTypeCard(
    title: String,
    subtitle: String,
    iconContent: @Composable () -> Unit,
    bgColor: Color,
    iconBgColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(24.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    color = Color(0xFF8B9D9F),
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
            
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                iconContent()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AccountSelectionScreenPreview() {
    AccountSelectionScreen()
}
