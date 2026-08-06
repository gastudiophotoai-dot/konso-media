package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KonsoTopBar(
    currentLanguage: String,
    activeRole: String,
    isVpnDetected: Boolean,
    isLoggedIn: Boolean,
    onToggleLanguage: () -> Unit,
    onRoleChange: (String) -> Unit,
    onOpenAuth: () -> Unit,
    onOpenProfile: () -> Unit,
    onLogout: () -> Unit
) {
    var showRoleDropdown by remember { mutableStateOf(false) }
    var showUserMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.konso_media_logo_1785918090982),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Konso Media",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Follower & Liker v1.0",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            // VPN Alert Indicator
            if (isVpnDetected) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(
                        text = "VPN ⚠️",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Language Switcher Button (EN / AM)
            OutlinedButton(
                onClick = onToggleLanguage,
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                modifier = Modifier
                    .height(32.dp)
                    .testTag("language_toggle_btn")
            ) {
                Text(text = if (currentLanguage == "EN") "🇪🇹 AM" else "🇬🇧 EN", fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.width(4.dp))

            if (isLoggedIn) {
                // Role Switcher Dropdown (USER / CLIENT / ADMIN)
                Box {
                    Button(
                        onClick = { showRoleDropdown = true },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier
                            .height(32.dp)
                            .testTag("role_selector_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Text(text = activeRole, fontSize = 11.sp, color = MaterialTheme.colorScheme.onPrimaryContainer, fontWeight = FontWeight.Bold)
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, modifier = Modifier.size(16.dp))
                    }

                    DropdownMenu(
                        expanded = showRoleDropdown,
                        onDismissRequest = { showRoleDropdown = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(if (currentLanguage == "AM") "የተጠቃሚ ስራ Mode" else "User / Earner Mode") },
                            onClick = {
                                onRoleChange("USER")
                                showRoleDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (currentLanguage == "AM") "የደንበኛ/ማስታወቂያ Mode" else "Client / Advertiser Mode") },
                            onClick = {
                                onRoleChange("CLIENT")
                                showRoleDropdown = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (currentLanguage == "AM") "የአስተዳዳሪ Panel Mode" else "Admin Panel Mode") },
                            onClick = {
                                onRoleChange("ADMIN")
                                showRoleDropdown = false
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Profile Button
                IconButton(
                    onClick = onOpenProfile,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("topbar_profile_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Logout Button
                IconButton(
                    onClick = onLogout,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("topbar_logout_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Logout,
                        contentDescription = "Log Out",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(20.dp)
                    )
                }
            } else {
                // Log In Button
                Button(
                    onClick = onOpenAuth,
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier
                        .height(32.dp)
                        .testTag("topbar_login_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Login, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (currentLanguage == "AM") "መግቢያ" else "Log In",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    )
}

@Composable
fun KonsoBottomNav(
    currentRoute: String,
    isAmharic: Boolean,
    activeRole: String,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
    ) {
        val items = listOf(
            Screen.Home to Icons.Default.Home,
            Screen.Dashboard to Icons.Default.AccountBalanceWallet,
            Screen.ClientCampaigns to Icons.Default.Campaign,
            Screen.Profile to Icons.Default.Person,
            Screen.AdminPanel to Icons.Default.AdminPanelSettings,
            Screen.Security to Icons.Default.Security
        )

        items.forEach { (screen, icon) ->
            // Highlight relevant items based on active role
            val isSelected = currentRoute == screen.route

            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(screen.route) },
                icon = { Icon(icon, contentDescription = screen.titleEN) },
                label = {
                    Text(
                        text = if (isAmharic) screen.titleAM else screen.titleEN,
                        fontSize = 10.sp,
                        maxLines = 1
                    )
                },
                modifier = Modifier.testTag("nav_${screen.route}")
            )
        }
    }
}
