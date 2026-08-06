package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.ui.components.CaptchaDialog
import com.example.ui.components.KonsoBottomNav
import com.example.ui.components.KonsoTopBar
import com.example.ui.components.OtpDialog
import com.example.ui.navigation.Screen
import com.example.ui.screens.*
import com.example.ui.theme.KonsoMediaTheme
import com.example.ui.viewmodel.KonsoViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: KonsoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            KonsoMediaTheme(darkTheme = false) {
                var currentRoute by remember { mutableStateOf(Screen.Home.route) }

                val currentUser by viewModel.currentUser.collectAsState()
                val transactions by viewModel.transactions.collectAsState()
                val campaigns by viewModel.campaigns.collectAsState()
                val notifications by viewModel.notifications.collectAsState()

                val currentLanguage by viewModel.currentLanguage.collectAsState()
                val activeRole by viewModel.activeRole.collectAsState()
                val isLoggedIn by viewModel.isLoggedIn.collectAsState()
                val authEmailOrPhone by viewModel.authEmailOrPhone.collectAsState()
                val isAdminVerified by viewModel.isAdminVerified.collectAsState()
                val isVpnDetected by viewModel.isVpnDetected.collectAsState()
                val isDeviceTrusted by viewModel.isDeviceTrusted.collectAsState()

                val showCaptchaModal by viewModel.showCaptchaModal.collectAsState()
                val showOtpModal by viewModel.showOtpModal.collectAsState()
                val uiMessage by viewModel.uiMessage.collectAsState()

                val snackbarHostState = remember { SnackbarHostState() }

                val isAmharic = currentLanguage == "AM"

                LaunchedEffect(uiMessage) {
                    uiMessage?.let {
                        snackbarHostState.showSnackbar(it)
                        viewModel.clearUiMessage()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        KonsoTopBar(
                            currentLanguage = currentLanguage,
                            activeRole = activeRole,
                            isVpnDetected = isVpnDetected,
                            isLoggedIn = isLoggedIn,
                            onToggleLanguage = { viewModel.toggleLanguage() },
                            onRoleChange = { role ->
                                viewModel.switchRole(role)
                                when (role) {
                                    "USER" -> currentRoute = Screen.Dashboard.route
                                    "CLIENT" -> currentRoute = Screen.ClientCampaigns.route
                                    "ADMIN" -> currentRoute = Screen.AdminPanel.route
                                }
                            },
                            onOpenAuth = { currentRoute = Screen.Auth.route },
                            onOpenProfile = { currentRoute = Screen.Profile.route },
                            onLogout = {
                                viewModel.logout()
                                currentRoute = Screen.Auth.route
                            }
                        )
                    },
                    bottomBar = {
                        KonsoBottomNav(
                            currentRoute = currentRoute,
                            isAmharic = isAmharic,
                            activeRole = activeRole,
                            onNavigate = { route -> currentRoute = route }
                        )
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentRoute) {
                            Screen.Auth.route -> AuthScreen(
                                isAmharic = isAmharic,
                                onLoginSuccess = { email, role ->
                                    viewModel.login(email, role)
                                    currentRoute = when (role) {
                                        "CLIENT" -> Screen.ClientCampaigns.route
                                        "ADMIN" -> Screen.AdminPanel.route
                                        else -> Screen.Dashboard.route
                                    }
                                },
                                onRegisterSuccess = { name, email, role ->
                                    viewModel.register(name, email, role)
                                    currentRoute = when (role) {
                                        "CLIENT" -> Screen.ClientCampaigns.route
                                        "ADMIN" -> Screen.AdminPanel.route
                                        else -> Screen.Dashboard.route
                                    }
                                },
                                onBackToHome = { currentRoute = Screen.Home.route }
                            )

                            Screen.Home.route -> HomeScreen(
                                isAmharic = isAmharic,
                                onNavigateToDashboard = {
                                    if (isLoggedIn) {
                                        currentRoute = Screen.Dashboard.route
                                    } else {
                                        currentRoute = Screen.Auth.route
                                    }
                                },
                                onNavigateToCampaigns = {
                                    if (isLoggedIn) {
                                        currentRoute = Screen.ClientCampaigns.route
                                    } else {
                                        currentRoute = Screen.Auth.route
                                    }
                                }
                            )

                            Screen.Dashboard.route -> UserDashboardScreen(
                                user = currentUser,
                                transactions = transactions,
                                activeCampaigns = campaigns,
                                notifications = notifications,
                                isAmharic = isAmharic,
                                onDeposit = { method, amount, ref -> viewModel.depositMoney(method, amount, ref) },
                                onWithdraw = { method, amount, acc -> viewModel.withdrawMoney(method, amount, acc) },
                                onCompleteTask = { campaign -> viewModel.completeTask(campaign) },
                                onMarkNotificationsRead = { viewModel.markAllNotificationsRead() },
                                onSecurityVerificationRequest = { action -> viewModel.triggerActionWithSecurity(action) {} }
                            )

                            Screen.ClientCampaigns.route -> ClientCampaignScreen(
                                campaigns = campaigns,
                                userBalance = currentUser?.balance ?: 0.0,
                                isAmharic = isAmharic,
                                onCreateCampaign = { client, plat, action, link, qty, price, country ->
                                    viewModel.createCampaign(client, plat, action, link, qty, price, country)
                                },
                                onPauseCampaign = { id -> viewModel.updateCampaignStatus(id, "PAUSED") },
                                onResumeCampaign = { id -> viewModel.updateCampaignStatus(id, "ACTIVE") }
                            )

                            Screen.AdminPanel.route -> AdminPanelScreen(
                                user = currentUser,
                                transactions = transactions,
                                campaigns = campaigns,
                                isAmharic = isAmharic,
                                authEmailOrPhone = authEmailOrPhone,
                                isAdminVerified = isAdminVerified,
                                onOpenAuth = { currentRoute = Screen.Auth.route },
                                onApproveTx = { id -> viewModel.approveTransaction(id) },
                                onRejectTx = { id -> viewModel.rejectTransaction(id) },
                                onUpdateCampaignStatus = { id, status -> viewModel.updateCampaignStatus(id, status) }
                            )

                            Screen.Profile.route -> ProfileScreen(
                                user = currentUser,
                                isAmharic = isAmharic,
                                authEmailOrPhone = authEmailOrPhone,
                                activeRole = activeRole,
                                onRoleChange = { role -> viewModel.switchRole(role) },
                                onUpdateProfile = { name, email, phone, service, payout, biometrics ->
                                    viewModel.updateUserProfile(name, email, phone, service, payout, biometrics)
                                },
                                onLogout = {
                                    viewModel.logout()
                                    currentRoute = Screen.Auth.route
                                }
                            )

                            Screen.Security.route -> SecurityScreen(
                                isVpnDetected = isVpnDetected,
                                isDeviceTrusted = isDeviceTrusted,
                                isAmharic = isAmharic,
                                onToggleVpn = { viewModel.toggleVpnSimulation() },
                                onTestCaptcha = { viewModel.triggerActionWithSecurity("Security CAPTCHA Test") {} },
                                onTestOtp = { viewModel.triggerActionWithSecurity("2FA OTP Test") {} }
                            )

                            Screen.Legal.route -> LegalScreen(isAmharic = isAmharic)
                        }

                        // Security Dialog Overlays
                        if (showCaptchaModal) {
                            CaptchaDialog(
                                onSuccess = { viewModel.onCaptchaSolved() },
                                onDismiss = { viewModel.dismissCaptcha() }
                            )
                        }

                        if (showOtpModal) {
                            OtpDialog(
                                phoneOrEmail = currentUser?.phone ?: "+251 91 123 4567",
                                onSuccess = { viewModel.onOtpVerified() },
                                onDismiss = { viewModel.dismissOtp() }
                            )
                        }
                    }
                }
            }
        }
    }
}
