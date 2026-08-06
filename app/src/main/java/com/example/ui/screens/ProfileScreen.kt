package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserEntity

@Composable
fun ProfileScreen(
    user: UserEntity?,
    isAmharic: Boolean,
    authEmailOrPhone: String,
    activeRole: String,
    onRoleChange: (String) -> Unit,
    onUpdateProfile: (name: String, email: String, phone: String, service: String, payout: String, biometrics: Boolean) -> Unit,
    onLogout: () -> Unit
) {
    var nameInput by remember(user) { mutableStateOf(user?.name ?: "Gele Zusha") }
    var emailInput by remember(authEmailOrPhone) { mutableStateOf(user?.email.takeIf { !it.isNullByBlank() } ?: authEmailOrPhone) }
    var phoneInput by remember(user) { mutableStateOf(user?.phone ?: "0912702062") }
    var servicePreference by remember { mutableStateOf("TikTok & Telegram Booster") }
    var payoutAccountInput by remember { mutableStateOf("Telebirr: 0912702062") }
    var isBiometricsEnabled by remember { mutableStateOf(true) }
    var securityQuestionAnswer by remember { mutableStateOf("Addis Ababa") }

    var isEditMode by remember { mutableStateOf(false) }
    var saveSuccessMessage by remember { mutableStateOf<String?>(null) }
    var isFingerprintScanning by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Profile Banner Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF2563EB), Color(0xFF4338CA))
                    )
                )
                .padding(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                // Profile Avatar Box with Edit Badge
                Box(contentAlignment = Alignment.BottomEnd) {
                    Surface(
                        shape = CircleShape,
                        color = Color.White,
                        modifier = Modifier
                            .size(80.dp)
                            .border(3.dp, Color(0xFF60A5FA), CircleShape)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = nameInput.take(2).uppercase(),
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF2563EB)
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Check, contentDescription = "Verified", tint = Color.White, modifier = Modifier.size(14.dp))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = nameInput,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = emailInput,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFFDBEAFE)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = "Role: $activeRole",
                            color = Color.White,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFD1FAE5)
                    ) {
                        Text(
                            text = if (isAmharic) "ተረጋግጧል (KYC Verified)" else "KYC Verified ✅",
                            color = Color(0xFF065F46),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }

        // Save Alert Banner
        saveSuccessMessage?.let { msg ->
            Surface(
                color = Color(0xFFD1FAE5),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF059669))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = msg, color = Color(0xFF065F46), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        // Section 1: Personal Profile Info (ስማቸው እና የግል መረጃዎች)
        Card(
            modifier = Modifier.fillMaxWidth().testTag("profile_personal_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAmharic) "የግል መረጃ (Personal Info)" else "Personal Profile",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(onClick = { isEditMode = !isEditMode }) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.Done else Icons.Default.Edit,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(if (isEditMode) (if (isAmharic) "ጨርስ" else "Done") else (if (isAmharic) "አስተካክል" else "Edit"))
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Name Input
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    enabled = isEditMode,
                    label = { Text(if (isAmharic) "ሙሉ ስም (Full Name)" else "Full Name") },
                    leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Gmail Input
                OutlinedTextField(
                    value = emailInput,
                    onValueChange = { emailInput = it },
                    enabled = isEditMode,
                    label = { Text(if (isAmharic) "ኢሜይል (Gmail Address)" else "Gmail Address") },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_email_input")
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Phone Input
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = { phoneInput = it },
                    enabled = isEditMode,
                    label = { Text(if (isAmharic) "ስልክ ቁጥር (Phone Number)" else "Phone Number") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("profile_phone_input")
                )
            }
        }

        // Section 2: Account Role & Services Preference (አገልግሎት እና የስራ ዘርፍ)
        Card(
            modifier = Modifier.fillMaxWidth().testTag("profile_services_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Work, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAmharic) "የአገልግሎት ዘርፍ (Services & Role)" else "Services & Preference",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isAmharic) "የአካውንት አይነት (Active Role):" else "Active Account Role:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val roles = listOf(
                        "USER" to if (isAmharic) "ተጠቃሚ (User)" else "User Earner",
                        "CLIENT" to if (isAmharic) "ደንበኛ (Client)" else "Client Advertiser",
                        "ADMIN" to if (isAmharic) "አስተዳዳሪ" else "Admin"
                    )

                    roles.forEach { (roleKey, label) ->
                        val isSelected = activeRole == roleKey
                        FilterChip(
                            selected = isSelected,
                            onClick = { onRoleChange(roleKey) },
                            label = { Text(text = label, fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = servicePreference,
                    onValueChange = { servicePreference = it },
                    enabled = isEditMode,
                    label = { Text(if (isAmharic) "የሚመርጡት የማህበራዊ ሚዲያ አገልግሎት" else "Preferred Media Service") },
                    leadingIcon = { Icon(Icons.Default.Category, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Section 3: Biometric Fingerprint & Security Settings (የጣት አሻራ እና ደህንነት)
        Card(
            modifier = Modifier.fillMaxWidth().testTag("profile_biometrics_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAmharic) "የጣት አሻራ እና ደህንነት (Biometric Security)" else "Biometrics & Security",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Biometrics Switch Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (isAmharic) "የጣት አሻራ መግቢያ (Fingerprint Unlock)" else "Fingerprint / Face Unlock",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isAmharic) "በጣት አሻራዎ በፍጥነት እና በደህንነት ይግቡ" else "Unlock app securely using device biometric sensors",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Switch(
                        checked = isBiometricsEnabled,
                        onCheckedChange = { isBiometricsEnabled = it },
                        modifier = Modifier.testTag("fingerprint_toggle_switch")
                    )
                }

                if (isBiometricsEnabled) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                isFingerprintScanning = true
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = if (isAmharic) "የጣት አሻራ ተመዝግቧል (Fingerprint Registered)" else "Biometric Sensor Active",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = if (isFingerprintScanning) (if (isAmharic) "ጣትዎን ዳሳሹ ላይ ያድርጉ... 🟢" else "Scanning fingerprint... 🟢") else (if (isAmharic) "ለማረጋገጥ እዚህ ይጫኑ" else "Tap here to test sensor"),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Security Question
                OutlinedTextField(
                    value = securityQuestionAnswer,
                    onValueChange = { securityQuestionAnswer = it },
                    enabled = isEditMode,
                    label = { Text(if (isAmharic) "የደህንነት ጥያቄ መልስ (የተወለዱበት ቦታ)" else "Security Question Answer") },
                    leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Section 4: Payout & Wallet Method (የክፍያ/የባንክ መረጃ)
        Card(
            modifier = Modifier.fillMaxWidth().testTag("profile_payout_card"),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AccountBalance, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isAmharic) "የክፍያ መቀበያ አድራሻ (Payout Account)" else "Payout & Wallet Setup",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = payoutAccountInput,
                    onValueChange = { payoutAccountInput = it },
                    enabled = isEditMode,
                    label = { Text(if (isAmharic) "Telebirr / የባንክ ሂሳብ ቁጥር" else "Telebirr / CBE Bank Account") },
                    leadingIcon = { Icon(Icons.Default.Payments, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // Save Profile Changes Button
        Button(
            onClick = {
                onUpdateProfile(nameInput, emailInput, phoneInput, servicePreference, payoutAccountInput, isBiometricsEnabled)
                saveSuccessMessage = if (isAmharic) "✅ ፕሮፋይልዎ በስኬት ተስተካክሏል!" else "✅ Profile details saved successfully!"
                isEditMode = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("save_profile_btn"),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAmharic) "ለውጦችን አስቀምጥ (Save Profile)" else "Save Profile Changes",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 16.sp
            )
        }

        // Log Out Button
        OutlinedButton(
            onClick = onLogout,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("profile_logout_btn"),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Icon(Icons.Default.Logout, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isAmharic) "ከመለያው ውጣ (Log Out)" else "Log Out",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

private fun String?.isNullByBlank(): Boolean = this.isNullOrBlank()
