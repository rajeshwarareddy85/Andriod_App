package com.simats.myapplication

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.*
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onCodeSent: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val cardBgColor = Color(0xFF142426)
    val textGray = Color(0xFF8B9D9F)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var email by rememberSaveable { mutableStateOf("") }
    var isLoading by rememberSaveable { mutableStateOf(false) }

    val canSubmit = email.trim().isNotEmpty() && !isLoading

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Forgot Password",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Reset your password",
            color = Color.White,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Enter your registered email address. We’ll send you a verification code.",
            color = textGray,
            fontSize = 14.sp,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = cyanColor
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = cardBgColor,
                unfocusedContainerColor = cardBgColor,
                focusedBorderColor = cyanColor,
                unfocusedBorderColor = Color(0xFF1B3B40),
                focusedLabelColor = cyanColor,
                unfocusedLabelColor = textGray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = cyanColor
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                val trimmedEmail = email.trim()

                if (trimmedEmail.isBlank()) {
                    Toast.makeText(context, "Please enter email", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    try {
                        isLoading = true

                        val response = ApiClient.api.forgotPassword(
                            ForgotPasswordRequest(email = trimmedEmail)
                        )

                        val body = response.body()

                        if (response.isSuccessful && body != null && body.ok) {
                            Toast.makeText(
                                context,
                                body.message ?: "Reset code sent successfully",
                                Toast.LENGTH_SHORT
                            ).show()

                            onCodeSent(trimmedEmail)
                        } else {
                            Toast.makeText(
                                context,
                                body?.error ?: body?.message ?: "Failed to send reset code",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            e.message ?: "Network error",
                            Toast.LENGTH_LONG
                        ).show()
                    } finally {
                        isLoading = false
                    }
                }
            },
            enabled = canSubmit,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = cyanColor)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.Black,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = "Send Code",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Back to Login",
            color = cyanColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.clickable { onBackClick() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ForgotPasswordScreenPreview() {
    ForgotPasswordScreen()
}