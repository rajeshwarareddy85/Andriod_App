package com.simats.myapplication

import android.widget.Toast
import androidx.compose.foundation.background
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
import com.simats.myapplication.model.*
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminSignUpScreen(
    onBackClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpSuccess: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val cyanColor = Color(0xFF00E5FF)
    val darkBgColor = Color(0xFF0C1D20)
    val inputBgColor = Color(0xFF14282B)
    val textGray = Color(0xFF8B9D9F)
    val labelGray = Color(0xFF4FA8B4)

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var orgName by remember { mutableStateOf("") }
    var utilityId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var agreedToTerms by remember { mutableStateOf(false) }

    var loading by remember { mutableStateOf(false) }
    var generatedBoardId by remember { mutableStateOf<String?>(null) }

    fun validate(): Boolean {
        if (fullName.trim().isEmpty()) {
            Toast.makeText(context, "Full name required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (orgName.trim().isEmpty()) {
            Toast.makeText(context, "Organization name required", Toast.LENGTH_SHORT).show()
            return false
        }
        if (email.trim().isEmpty() || !email.contains("@") || !email.contains(".")) {
            Toast.makeText(context, "Enter valid email", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }
        if (password != confirmPassword) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!agreedToTerms) {
            Toast.makeText(context, "Please accept terms", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(darkBgColor)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(70.dp)
                .shadow(
                    elevation = 15.dp,
                    shape = CircleShape,
                    ambientColor = cyanColor,
                    spotColor = cyanColor
                ),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(cyanColor.copy(alpha = 0.3f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Admin Shield",
                    tint = cyanColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        Text(
            text = "PowerPulse Admin",
            color = Color.White,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "DISTRIBUTOR REGISTRATION",
            color = textGray,
            fontSize = 12.sp,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(30.dp))

        Text(
            text = "Create Admin Account",
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))

        AdminLabel("FULL NAME", labelGray)
        AdminTextField(
            value = fullName,
            onValueChange = { fullName = it },
            placeholder = "John Doe",
            leadingIcon = Icons.Default.Person,
            inputBgColor = inputBgColor,
            cyanColor = cyanColor,
            textGray = textGray
        )

        Spacer(Modifier.height(16.dp))

        AdminLabel("OFFICIAL EMAIL ADDRESS", labelGray)
        AdminTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "admin@utilityboard.gov",
            leadingIcon = Icons.Default.Email,
            inputBgColor = inputBgColor,
            cyanColor = cyanColor,
            textGray = textGray
        )

        Spacer(Modifier.height(16.dp))

        AdminLabel("ORGANIZATION NAME", labelGray)
        AdminTextField(
            value = orgName,
            onValueChange = { orgName = it },
            placeholder = "e.g. Metropolis Energy Corp",
            leadingIcon = Icons.Default.Business,
            inputBgColor = inputBgColor,
            cyanColor = cyanColor,
            textGray = textGray
        )

        Spacer(Modifier.height(16.dp))

        AdminLabel("UTILITY BOARD ID (OPTIONAL)", labelGray)
        AdminTextField(
            value = utilityId,
            onValueChange = { utilityId = it },
            placeholder = "Leave empty to auto-generate",
            leadingIcon = Icons.Default.Badge,
            inputBgColor = inputBgColor,
            cyanColor = cyanColor,
            textGray = textGray
        )

        Spacer(Modifier.height(16.dp))

        AdminLabel("CREATE SECURE PASSWORD", labelGray)
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

        Spacer(Modifier.height(16.dp))

        AdminLabel("CONFIRM PASSWORD", labelGray)
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = { Text("........... ", color = Color(0xFF2C4E52)) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = cyanColor, modifier = Modifier.size(20.dp)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = adminTextFieldColors(inputBgColor, cyanColor, textGray)
        )

        Spacer(Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Checkbox(
                checked = agreedToTerms,
                onCheckedChange = { agreedToTerms = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = cyanColor,
                    uncheckedColor = Color(0xFF1E3336),
                    checkmarkColor = Color.Black
                )
            )
            Text(
                text = "I agree to the Data Security & Privacy Policy for distributor administrators.",
                color = textGray,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }

        if (generatedBoardId != null) {
            Spacer(Modifier.height(10.dp))
            Text(
                text = "✅ Your Board ID: $generatedBoardId",
                color = cyanColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (validate() && !loading) {
                    scope.launch {
                        loading = true
                        generatedBoardId = null

                        try {
                            val req = AdminRegisterReq(
                                fullName = fullName.trim(),
                                orgName = orgName.trim(),
                                boardId = utilityId.trim().ifEmpty { "" },
                                email = email.trim().lowercase(),
                                password = password,
                                confirmPassword = confirmPassword
                            )

                            val res = ApiClient.api.registerAdmin(req)

                            if (res.isSuccessful && res.body()?.ok == true) {
                                val boardId = res.body()?.boardId
                                generatedBoardId = boardId
                                Toast.makeText(context, "Admin created successfully", Toast.LENGTH_SHORT).show()
                                onSignUpSuccess(email.trim().lowercase())
                            } else {
                                val msg = res.body()?.error ?: res.body()?.message ?: "Registration failed"
                                Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_LONG).show()
                        } finally {
                            loading = false
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(10.dp, RoundedCornerShape(14.dp), spotColor = cyanColor),
            colors = ButtonDefaults.buttonColors(containerColor = cyanColor),
            shape = RoundedCornerShape(14.dp),
            enabled = !loading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )
                    Spacer(Modifier.width(10.dp))
                } else {
                    Icon(Icons.Default.PersonAdd, contentDescription = null, tint = Color.Black)
                    Spacer(Modifier.width(8.dp))
                }
                Text(
                    if (loading) "CREATING..." else "CREATE ADMIN ACCOUNT",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Row {
            Text("Already have an account? ", color = textGray, fontSize = 14.sp)
            Text(
                "Sign In",
                color = cyanColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }

        Spacer(Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Shield, null, tint = textGray, modifier = Modifier.size(12.dp))
            Spacer(Modifier.width(4.dp))
            Text("SECURE 256-BIT ENCRYPTED CONNECTION", color = textGray, fontSize = 10.sp)
        }

        Spacer(Modifier.height(20.dp))
    }
}

@Composable
fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    inputBgColor: Color,
    cyanColor: Color,
    textGray: Color
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = Color(0xFF2C4E52)) },
        leadingIcon = { Icon(leadingIcon, null, tint = cyanColor, modifier = Modifier.size(20.dp)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = adminTextFieldColors(inputBgColor, cyanColor, textGray)
    )
}