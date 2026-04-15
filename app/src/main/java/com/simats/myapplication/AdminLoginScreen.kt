package com.simats.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import android.widget.Toast
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.AdminLoginReq
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    onBackClick: () -> Unit,
    onLoginSuccess: (String) -> Unit, // ✅ renamed (was onLoginClick)
    onSignUpClick: () -> Unit,
    onRecoveryClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cyanColor = Color(0xFF00E5FF)
    val darkBgColor = Color(0xFF0C1D20)
    val inputBgColor = Color(0xFF14282B)
    val textGray = Color(0xFF8B9D9F)
    val labelGray = Color(0xFF4FA8B4)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Spacer(Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .shadow(
                    elevation = 20.dp,
                    shape = CircleShape,
                    ambientColor = cyanColor,
                    spotColor = cyanColor
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(cyanColor.copy(alpha = 0.4f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Admin Shield",
                    tint = cyanColor,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "PowerPulse Admin",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Prepaid Electricity Management",
            color = textGray,
            fontSize = 14.sp
        )

        Spacer(Modifier.height(40.dp))

        Text(
            text = "Distributor Secure Access",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(24.dp))

        AdminLabel("USERNAME / EMAIL", labelGray)
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("e.g. admin@utility.com", color = Color(0xFF2C4E52)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = cyanColor,
                    modifier = Modifier.size(20.dp)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = adminTextFieldColors(inputBgColor, cyanColor, textGray)
        )

        Spacer(Modifier.height(20.dp))

        AdminLabel("SECURITY PASSWORD", labelGray)
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("........... ", color = Color(0xFF2C4E52)) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = cyanColor, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = Color(0xFF2C4E52)
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = adminTextFieldColors(inputBgColor, cyanColor, textGray)
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = "Recovery access?",
            color = cyanColor,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onRecoveryClick(username.trim()) }, // ✅ trim
            textAlign = TextAlign.End
        )

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                val cleanEmail = username.trim().lowercase()
                val cleanPassword = password.trim()

                if (cleanEmail.isEmpty()) {
                    Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                } else if (cleanPassword.isEmpty()) {
                    Toast.makeText(context, "Please enter password", Toast.LENGTH_SHORT).show()
                } else {
                    scope.launch {
                        try {
                            isLoading = true
                            val req = AdminLoginReq(
                                orgName = "", // UI simplified, assuming backend handles this
                                boardId = "", // UI simplified, assuming backend handles this
                                email = cleanEmail,
                                password = cleanPassword
                            )

                            val response = ApiClient.api.loginAdmin(req)
                            val body = response.body()

                            if (response.isSuccessful && body != null && body.ok) {
                                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                onLoginSuccess(cleanEmail)
                            } else {
                                val errorMsg = body?.error ?: body?.message ?: "Invalid credentials"
                                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            isLoading = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
            shape = RoundedCornerShape(14.dp),
            enabled = !isLoading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.Black,
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                    Text("SIGN IN", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onSignUpClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = cyanColor)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null, tint = cyanColor)
                Spacer(Modifier.width(8.dp))
                Text("SIGN UP", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, null, tint = textGray, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(4.dp))
            Text("SECURE 256-BIT ENCRYPTED CONNECTION", color = textGray, fontSize = 10.sp)
        }

        Spacer(Modifier.height(12.dp))

        Row {
            Text("Contact Support", color = textGray, fontSize = 12.sp)
            Text("  •  ", color = textGray, fontSize = 12.sp)
            Text("System Status", color = textGray, fontSize = 12.sp)
        }

        Spacer(Modifier.height(32.dp))
    }
}