package com.simats.PowerPulse

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.PowerPulse.repository.AuthRepository
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordScreen(
    email: String,
    otp: String,
    onResetSuccess: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val repository = remember { AuthRepository() }

    val darkBg = Color(0xFF0C1D20)
    val cyan = Color(0xFF00E5FF)
    val cardBg = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)

    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBg)
            .padding(24.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(28.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Reset Password",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = cardBg)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Reset password for",
                        color = textGray,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = email,
                        color = cyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            when {
                                otp.isBlank() -> {
                                    Toast.makeText(context, "OTP missing", Toast.LENGTH_SHORT).show()
                                }
                                newPassword.isBlank() -> {
                                    Toast.makeText(context, "Enter new password", Toast.LENGTH_SHORT).show()
                                }
                                confirmPassword.isBlank() -> {
                                    Toast.makeText(context, "Confirm your password", Toast.LENGTH_SHORT).show()
                                }
                                newPassword != confirmPassword -> {
                                    Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
                                }
                                else -> {
                                    scope.launch {
                                        isLoading = true

                                        repository.resetPassword(
                                            email = email,
                                            otp = otp,
                                            newPassword = newPassword,
                                            confirmPassword = confirmPassword
                                        )
                                            .onSuccess { response ->
                                                isLoading = false
                                                if (response.ok) {
                                                    Toast.makeText(
                                                        context,
                                                        response.message ?: "Password reset successful",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    onResetSuccess()
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        response.error ?: "Password reset failed",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                            .onFailure { error ->
                                                isLoading = false
                                                Toast.makeText(
                                                    context,
                                                    error.message ?: "Something went wrong",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = cyan),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.Black,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(22.dp)
                            )
                        } else {
                            Text(
                                text = "Reset Password",
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}