package com.simats.PowerPulse

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.PowerPulse.repository.AuthRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConsumerSignUpScreen(
    onBackClick: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onCreateAccountSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val cyanColor = Color(0xFF00E5FF)
    val darkBgColor = Color(0xFF0C1D20)
    val inputBgColor = Color(0xFF14282B)
    val textGray = Color(0xFF8B9D9F)

    var fullName by remember { mutableStateOf("") }
    var mandal by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var agreeToTerms by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val authRepository = remember { AuthRepository() }

    val canCreate =
        fullName.trim().isNotEmpty() &&
                mandal.trim().isNotEmpty() &&
                email.trim().isNotEmpty() &&
                password.isNotEmpty() &&
                confirmPassword.isNotEmpty() &&
                (password == confirmPassword) &&
                agreeToTerms

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { onBackClick() }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(cyanColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.size(12.dp)) {
                            val path = Path().apply {
                                moveTo(size.width * 0.6f, size.height * 0.1f)
                                lineTo(size.width * 0.2f, size.height * 0.55f)
                                lineTo(size.width * 0.45f, size.height * 0.55f)
                                lineTo(size.width * 0.35f, size.height * 0.9f)
                                lineTo(size.width * 0.8f, size.height * 0.45f)
                                lineTo(size.width * 0.55f, size.height * 0.45f)
                                close()
                            }
                            drawPath(path = path, color = Color.Black)
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "PowerPulse",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Box(modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Track and predict your electricity\nconsumption with precision.",
                color = textGray,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            SignUpInputField(
                label = "Full Name",
                value = fullName,
                onValueChange = { fullName = it },
                placeholder = "John Doe",
                leadingIcon = {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        tint = textGray
                    )
                },
                inputBgColor = inputBgColor,
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpInputField(
                label = "Mandal",
                value = mandal,
                onValueChange = { mandal = it },
                placeholder = "Ex: Zone A",
                leadingIcon = {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = null,
                        tint = textGray
                    )
                },
                inputBgColor = inputBgColor,
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpInputField(
                label = "Email or Phone",
                value = email,
                onValueChange = { email = it },
                placeholder = "name@example.com",
                leadingIcon = {
                    Text(
                        "@",
                        color = textGray,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                inputBgColor = inputBgColor,
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpInputField(
                label = "Password",
                value = password,
                onValueChange = { password = it },
                placeholder = "••••••••",
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = textGray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = textGray
                        )
                    }
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                inputBgColor = inputBgColor,
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpInputField(
                label = "Confirm Password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = "••••••••",
                leadingIcon = {
                    Icon(
                        Icons.Default.Lock,
                        contentDescription = null,
                        tint = textGray
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(
                            imageVector = if (confirmPasswordVisible) {
                                Icons.Default.Visibility
                            } else {
                                Icons.Default.VisibilityOff
                            },
                            contentDescription = if (confirmPasswordVisible) "Hide confirm password" else "Show confirm password",
                            tint = textGray
                        )
                    }
                },
                visualTransformation = if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                inputBgColor = inputBgColor,
                cyanColor = cyanColor,
                textGray = textGray
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = agreeToTerms,
                    onCheckedChange = { agreeToTerms = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = cyanColor,
                        uncheckedColor = textGray,
                        checkmarkColor = Color.Black
                    )
                )
                Text(
                    text = "I agree to the Terms & Conditions and Privacy Policy",
                    color = Color.White,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    isLoading = true
                    scope.launch {
                        val result = authRepository.registerUser(
                            fullName = fullName.trim(),
                            mandal = mandal.trim(),
                            email = email.trim(),
                            password = password,
                            confirmPassword = confirmPassword
                        )
                        isLoading = false

                        result.onSuccess { msg ->
                            Toast.makeText(
                                context,
                                msg.toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            onCreateAccountSuccess()
                        }.onFailure { error ->
                            Toast.makeText(
                                context,
                                error.message ?: "Registration Failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                },
                enabled = canCreate && !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "Create Account",
                        color = Color.Black,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.padding(bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = textGray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Log In",
                    color = cyanColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onLoginClick() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    inputBgColor: Color,
    cyanColor: Color,
    textGray: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = textGray) },
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            visualTransformation = visualTransformation,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = inputBgColor,
                unfocusedContainerColor = inputBgColor,
                disabledContainerColor = inputBgColor,
                focusedBorderColor = cyanColor,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = cyanColor
            ),
            singleLine = true
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConsumerSignUpScreenPreview() {
    ConsumerSignUpScreen()
}