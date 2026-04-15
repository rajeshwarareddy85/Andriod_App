package com.simats.myapplication

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.myapplication.model.AdminProfileData
import com.simats.myapplication.network.ApiClient
import kotlinx.coroutines.launch

@Composable
fun AdminProfileScreen(
    adminEmail: String,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val darkBgColor = Color(0xFF0C1D20)
    val cyanColor = Color(0xFF00E5FF)
    val cardBgColor = Color(0xFF142426)
    val dividerColor = Color(0xFF1B3B3E)
    val textGray = Color(0xFF8B9D9F)

    var adminProfile by remember { mutableStateOf<AdminProfileData?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(adminEmail) {
        if (adminEmail.isNotEmpty()) {
            try {
                val response = ApiClient.api.getAdminProfileByEmail(adminEmail)
                if (response.isSuccessful && response.body()?.ok == true) {
                    adminProfile = response.body()?.admin
                } else {
                    errorMessage = response.body()?.error ?: "Failed to load profile"
                }
            } catch (e: Exception) {
                errorMessage = e.message ?: "Unknown error"
            } finally {
                isLoading = false
            }
        } else {
            isLoading = false
            errorMessage = "No admin email found"
        }
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(darkBgColor)
                    .padding(horizontal = 16.dp, vertical = 40.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .clickable { onBackClick() }
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = cyanColor, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(4.dp))
                        Text("Home", color = cyanColor, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(Modifier.weight(1f))

                    Text("Profile", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        },
        containerColor = darkBgColor
    ) { innerPadding ->
        if (isLoading) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = cyanColor)
            }
        } else if (errorMessage != null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                Text(errorMessage!!, color = Color.Red)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile Header
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .border(2.dp, cyanColor, CircleShape)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(cardBgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = cyanColor,
                        modifier = Modifier.size(80.dp)
                    )
                }

                Spacer(Modifier.height(20.dp))

                Text(
                    text = adminProfile?.fullName ?: "Admin User",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Professional Administrator",
                    color = cyanColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(Modifier.height(40.dp))

                // Admin Details
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(cardBgColor)
                        .border(1.dp, dividerColor, RoundedCornerShape(20.dp))
                        .padding(20.dp)
                ) {
                    DetailRow("Full Name", adminProfile?.fullName ?: "N/A", Icons.Default.Badge, cyanColor, textGray)
                    Divider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                    DetailRow("Admin ID", "#${adminProfile?.id ?: "N/A"}", Icons.Default.ConfirmationNumber, cyanColor, textGray)
                    Divider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                    DetailRow("Email Address", adminProfile?.email ?: "N/A", Icons.Default.Email, cyanColor, textGray)
                    Divider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                    DetailRow("Board ID", adminProfile?.boardId ?: "N/A", Icons.Default.Fingerprint, cyanColor, textGray)
                    Divider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                    DetailRow("Organization", adminProfile?.orgName ?: "N/A", Icons.Default.Business, cyanColor, textGray)
                    
                    val joinDate = adminProfile?.createdAt?.split("T")?.get(0) ?: "N/A"
                    Divider(color = dividerColor, thickness = 1.dp, modifier = Modifier.padding(vertical = 16.dp))
                    DetailRow("Account Created", joinDate, Icons.Default.CalendarToday, cyanColor, textGray)
                }

                Spacer(Modifier.height(40.dp))

                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935).copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFE53935).copy(alpha = 0.5f))
                ) {
                    Text("Logout Session", color = Color(0xFFE53935), fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(40.dp))
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String, icon: ImageVector, cyanColor: Color, textGray: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(cyanColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = cyanColor, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(text = label, color = textGray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(text = value, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}
