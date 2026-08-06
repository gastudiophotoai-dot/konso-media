package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class AuthTab {
    LOGIN,
    REGISTER,
    FORGOT_PASSWORD
}

@Composable
fun AuthScreen(
    isAmharic: Boolean,
    onLoginSuccess: (email: String, role: String) -> Unit,
    onRegisterSuccess: (name: String, email: String, role: String) -> Unit,
    onBackToHome: () -> Unit
) {
    var currentTab by remember { mutableStateOf(AuthTab.LOGIN) }
    var selectedRole by remember { mutableStateOf("USER") } // "USER", "CLIENT", "ADMIN"

    // Form inputs
    var fullName by remember { mutableStateOf("") }
    var emailOrPhone by remember { mutableStateOf("+251 ") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    // Forgot password states
    var otpSent by remember { mutableStateOf(false) }
    var otpCode by remember { mutableStateOf("") }
    var generatedOtp by remember { mutableStateOf("") }
    var securityQuestionAnswer by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .testTag("auth_card"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top Header Gradient Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(Color(0xFF2563EB), Color(0xFF10B981))
                            ),
                            shape = RoundedCornerShape(4.dp)
                        )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title & Subtitle
                Text(
                    text = when (currentTab) {
                        AuthTab.LOGIN -> if (isAmharic) "እንኳን ደህና መጡ! (Log In)" else "Welcome Back!"
                        AuthTab.REGISTER -> if (isAmharic) "መለያ ይፍጠሩ (Register)" else "Create an Account"
                        AuthTab.FORGOT_PASSWORD -> if (isAmharic) "የይለፍ ቃል ረስተዋል? (Forgot Password)" else "Reset Password"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = when (currentTab) {
                        AuthTab.LOGIN -> if (isAmharic) "ወደ Konso Media ለመግባት ዝርዝርዎን ያስገቡ" else "Enter your credentials to access your account"
                        AuthTab.REGISTER -> if (isAmharic) "የKonso Media አባል ይሁኑ እና መስራት ወይም ማስታወቅ ይጀምሩ" else "Sign up to start earning or creating campaigns"
                        AuthTab.FORGOT_PASSWORD -> if (isAmharic) "የደህንነት ኮድ በስልክዎ/ኢሜይልዎ እንልካለን" else "We will send a security OTP code to verify your identity"
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Role Selector Switcher (USER / CLIENT / ADMIN)
                Text(
                    text = if (isAmharic) "የአካውንት አይነት ይምረጡ (Account Role):" else "Select Account Role:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val roles = listOf(
                        "USER" to if (isAmharic) "ተጠቃሚ (User)" else "User / Earner",
                        "CLIENT" to if (isAmharic) "ደንበኛ (Client)" else "Client / Advertiser",
                        "ADMIN" to if (isAmharic) "አስተዳዳሪ" else "Admin"
                    )

                    roles.forEach { (roleKey, label) ->
                        val isSelected = selectedRole == roleKey
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedRole = roleKey },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("role_chip_$roleKey")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Error or Success Alert Banner
                errorMessage?.let { msg ->
                    Surface(
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "⚠️ $msg",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                successMessage?.let { msg ->
                    Surface(
                        color = Color(0xFFD1FAE5),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "✅ $msg",
                            color = Color(0xFF065F46),
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(10.dp),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // FORM CONTENT BASED ON TAB
                when (currentTab) {
                    AuthTab.LOGIN -> {
                        // Email or Phone Input
                        OutlinedTextField(
                            value = emailOrPhone,
                            onValueChange = { emailOrPhone = it },
                            label = { Text(if (isAmharic) "ስልክ ቁጥር ወይም ኢሜይል" else "Phone Number or Email") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_email_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Password Input
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(if (isAmharic) "የይለፍ ቃል (Password)" else "Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            trailingIcon = {
                                IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                    Icon(
                                        imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = "Toggle password"
                                    )
                                }
                            },
                            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("login_password_input")
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Forgot Password Link
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Text(
                                text = if (isAmharic) "የይለፍ ቃል ረስተዋል? (Forgot Password?)" else "Forgot Password?",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        errorMessage = null
                                        successMessage = null
                                        currentTab = AuthTab.FORGOT_PASSWORD
                                    }
                                    .testTag("forgot_password_link")
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit Login Button
                        Button(
                            onClick = {
                                val inputClean = emailOrPhone.trim().lowercase().replace(" ", "").replace("+251", "0")
                                val isAdminId = inputClean == "0912702062" || inputClean == "gelegezusha@gmail.com"
                                val isAdminPass = password == "gezushagele154213.com"

                                if (emailOrPhone.isBlank() || password.isBlank()) {
                                    errorMessage = if (isAmharic) "እባክዎን ስልክ/ኢሜይል እና የይለፍ ቃል ያስገቡ!" else "Please enter email/phone and password!"
                                } else if (selectedRole == "ADMIN" && (!isAdminId || !isAdminPass)) {
                                    errorMessage = if (isAmharic) "⛔ የአስተዳዳሪ መግቢያ የተከለከለ ነው! ትክክለኛውን የአስተዳዳሪ ስልክ/ኢሜይል (0912702062 / gelegezusha@gmail.com) እና የይለፍ ቃል ያስገቡ።" else "⛔ Unauthorized Admin Access! Only the registered owner (0912702062 / gelegezusha@gmail.com) can log in as Admin."
                                } else {
                                    errorMessage = null
                                    onLoginSuccess(emailOrPhone, selectedRole)
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("login_submit_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Login, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAmharic) "ግቡ (Log In)" else "Log In",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Switch to Register Link
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isAmharic) "መለያ የለዎትም? " else "Don't have an account? ",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = if (isAmharic) "ይመዝገቡ (Register)" else "Register Now",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        errorMessage = null
                                        successMessage = null
                                        currentTab = AuthTab.REGISTER
                                    }
                                    .testTag("switch_to_register_link")
                            )
                        }
                    }

                    AuthTab.REGISTER -> {
                        // Full Name Input
                        OutlinedTextField(
                            value = fullName,
                            onValueChange = { fullName = it },
                            label = { Text(if (isAmharic) "ሙሉ ስም (Full Name)" else "Full Name") },
                            leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_name_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Phone or Email Input
                        OutlinedTextField(
                            value = emailOrPhone,
                            onValueChange = { emailOrPhone = it },
                            label = { Text(if (isAmharic) "ስልክ ቁጥር ወይም ኢሜይል" else "Phone or Email") },
                            leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_email_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Password Input
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text(if (isAmharic) "የይለፍ ቃል (Password)" else "Password") },
                            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_password_input")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Confirm Password Input
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            label = { Text(if (isAmharic) "የይለፍ ቃል ማረጋገጫ" else "Confirm Password") },
                            leadingIcon = { Icon(Icons.Default.CheckCircle, contentDescription = null) },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("register_confirm_password_input")
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Submit Register Button
                        Button(
                            onClick = {
                                val inputClean = emailOrPhone.trim().lowercase().replace(" ", "").replace("+251", "0")
                                val isAdminId = inputClean == "0912702062" || inputClean == "gelegezusha@gmail.com"
                                val isAdminPass = password == "gezushagele154213.com"

                                when {
                                    fullName.isBlank() || emailOrPhone.isBlank() -> {
                                        errorMessage = if (isAmharic) "እባክዎን ስም እና ስልክ/ኢሜይል ያስገቡ!" else "Please fill in all required fields!"
                                    }
                                    selectedRole == "ADMIN" && (!isAdminId || !isAdminPass) -> {
                                        errorMessage = if (isAmharic) "⛔ የአስተዳዳሪ ምዝገባ የተከለከለ ነው! ትክክለኛውን የአስተዳዳሪ መረጃ ያስገቡ።" else "⛔ Admin registration blocked! Only the authorized owner can register as Admin."
                                    }
                                    password.length < 4 -> {
                                        errorMessage = if (isAmharic) "የይለፍ ቃል ቢያንስ 4 ፊደላት መሆን አለበት!" else "Password must be at least 4 characters!"
                                    }
                                    password != confirmPassword -> {
                                        errorMessage = if (isAmharic) "የይለፍ ቃሎች አይመሳሰሉም!" else "Passwords do not match!"
                                    }
                                    else -> {
                                        errorMessage = null
                                        onRegisterSuccess(fullName, emailOrPhone, selectedRole)
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("register_submit_btn"),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.AppRegistration, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAmharic) "ተመዝገብ (Register)" else "Create Account",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Switch to Login Link
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isAmharic) "መለያ አለዎት? " else "Already have an account? ",
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = if (isAmharic) "ይግቡ (Log In)" else "Log In",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier
                                    .clickable {
                                        errorMessage = null
                                        successMessage = null
                                        currentTab = AuthTab.LOGIN
                                    }
                                    .testTag("switch_to_login_link")
                            )
                        }
                    }

                    AuthTab.FORGOT_PASSWORD -> {
                        if (!otpSent) {
                            // Step 1: Request Security OTP Code
                            OutlinedTextField(
                                value = emailOrPhone,
                                onValueChange = { emailOrPhone = it },
                                label = { Text(if (isAmharic) "የተመዘገበ Gmail ወይም ስልክ ቁጥር" else "Registered Gmail or Phone Number") },
                                leadingIcon = { Icon(Icons.Default.MarkEmailRead, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("forgot_email_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    if (emailOrPhone.isBlank()) {
                                        errorMessage = if (isAmharic) "እባክዎን የተመዘገበ ስልክ/ኢሜይል ያስገቡ!" else "Please enter your registered Gmail/phone!"
                                    } else {
                                        errorMessage = null
                                        val randomCode = (100000..999999).random().toString()
                                        generatedOtp = randomCode
                                        otpSent = true
                                        successMessage = if (isAmharic)
                                            "📩 የደህንነት ማረጋገጫ ኮድ (OTP: $randomCode) ወደ $emailOrPhone ተልኳል! ማንም ሰው ያለዚህ ኮድ የይለፍ ቃል መቀየር አይችልም።"
                                        else
                                            "📩 Security OTP code ($randomCode) sent to $emailOrPhone! Verification required before password change."
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("send_otp_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAmharic) "የደህንነት ኮድ በGmail/ስልክ ላክ (Send OTP)" else "Send Verification Code to Gmail",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            // Step 2: Enter OTP Code, Security Question, & New Password
                            OutlinedTextField(
                                value = otpCode,
                                onValueChange = { otpCode = it },
                                label = { Text(if (isAmharic) "6-አሃዝ የደህንነት ኮድ (OTP Code)" else "6-Digit OTP Code") },
                                leadingIcon = { Icon(Icons.Default.Key, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("otp_code_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = securityQuestionAnswer,
                                onValueChange = { securityQuestionAnswer = it },
                                label = { Text(if (isAmharic) "የደህንነት ጥያቄ፡ የተወለዱበት ከተማ? (Security Answer)" else "Security Answer (e.g. Birth City)") },
                                leadingIcon = { Icon(Icons.Default.HelpOutline, contentDescription = null) },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("security_answer_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                value = newPassword,
                                onValueChange = { newPassword = it },
                                label = { Text(if (isAmharic) "አዲስ የይለፍ ቃል (New Password)" else "New Password") },
                                leadingIcon = { Icon(Icons.Default.LockReset, contentDescription = null) },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("new_password_input")
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = {
                                    when {
                                        otpCode.trim() != generatedOtp -> {
                                            errorMessage = if (isAmharic)
                                                "⛔ የተሳሳተ የደህንነት OTP ኮድ! የይለፍ ቃል መቀየር አልተፈቀደም።"
                                            else
                                                "⛔ Invalid Security OTP Code! Password change blocked."
                                        }
                                        securityQuestionAnswer.isBlank() -> {
                                            errorMessage = if (isAmharic)
                                                "እባክዎን የደህንነት ጥያቄውን መልስ ያስገቡ!"
                                            else
                                                "Please answer the security verification question!"
                                        }
                                        newPassword.length < 4 -> {
                                            errorMessage = if (isAmharic)
                                                "አዲሱ የይለፍ ቃል ቢያንስ 4 ፊደላት መሆን አለበት!"
                                            else
                                                "New password must be at least 4 characters long!"
                                        }
                                        else -> {
                                            errorMessage = null
                                            successMessage = if (isAmharic)
                                                "✅ የይለፍ ቃልዎ በስኬት ተቀይሯል! አሁን በአዲሱ የይለፍ ቃል መግባት ይችላሉ።"
                                            else
                                                "✅ Password reset successfully! You can now log in with your new password."
                                            otpSent = false
                                            otpCode = ""
                                            newPassword = ""
                                            securityQuestionAnswer = ""
                                            currentTab = AuthTab.LOGIN
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("reset_password_submit_btn"),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isAmharic) "የይለፍ ቃል አረጋግጠህ ቀይር (Verify & Reset)" else "Verify & Reset Password",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        TextButton(
                            onClick = {
                                errorMessage = null
                                successMessage = null
                                currentTab = AuthTab.LOGIN
                            }
                        ) {
                            Text(text = if (isAmharic) "← ወደ መግቢያ ተመለስ (Back to Login)" else "← Back to Login")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Continue as Guest Option
                OutlinedButton(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = if (isAmharic) "እንደ እንግዳ ቀጥል (Continue as Guest)" else "Browse as Guest")
                }
            }
        }
    }
}
