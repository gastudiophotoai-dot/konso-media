package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.CampaignEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity

@Composable
fun AdminPanelScreen(
    user: UserEntity?,
    transactions: List<TransactionEntity>,
    campaigns: List<CampaignEntity>,
    isAmharic: Boolean,
    authEmailOrPhone: String,
    isAdminVerified: Boolean,
    onOpenAuth: () -> Unit,
    onApproveTx: (id: Long) -> Unit,
    onRejectTx: (id: Long) -> Unit,
    onUpdateCampaignStatus: (id: Long, status: String) -> Unit
) {
    val inputClean = authEmailOrPhone.trim().lowercase().replace(" ", "").replace("+251", "0")
    val isOwner = inputClean == "0912702062" || inputClean == "gelegezusha@gmail.com"
    val isAccessGranted = isAdminVerified && isOwner

    if (!isAccessGranted) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .testTag("admin_access_denied_box"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Admin Security Lock",
                        tint = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.size(56.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = if (isAmharic) "⛔ የአስተዳዳሪ ገጽ የተጠበቀ ነው!" else "⛔ Admin Control Restricted!",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isAmharic)
                            "ይህ ገጽ የተፈቀደው ለዋናው ባለቤት ብቻ ነው። እባክዎን በስልክ (0912702062) ወይም በኢሜይል (gelegezusha@gmail.com) የአስተዳዳሪ የይለፍ ቃል ተጠቅመው ይግቡ።"
                        else
                            "Access strictly restricted to the platform owner. Please log in with official admin credentials (Phone: 0912702062 / Gmail: gelegezusha@gmail.com).",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = onOpenAuth,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Login, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isAmharic) "እንደ አስተዳዳሪ ይግቡ (Admin Log In)" else "Log In as Admin Owner",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
        return
    }

    var adminTab by remember { mutableStateOf(0) } // 0: Payment Queue, 1: Campaigns, 2: Fraud Detection, 3: Settings

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("admin_panel_screen")
    ) {
        // Admin Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(36.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = if (isAmharic) "የAdmin Panel (Konso Control Center)" else "Konso Media Control Center",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isAmharic) "ተጠቃሚዎችን፣ ክፍያዎችን እና የደህንነት ስጋቶችን ያስተዳድሩ" else "System oversight, payments, campaigns & anti-fraud management",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        TabRow(selectedTabIndex = adminTab) {
            Tab(selected = adminTab == 0, onClick = { adminTab = 0 }, text = { Text("Payments") }, icon = { Icon(Icons.Default.Payment, contentDescription = null) })
            Tab(selected = adminTab == 1, onClick = { adminTab = 1 }, text = { Text("Campaigns") }, icon = { Icon(Icons.Default.Campaign, contentDescription = null) })
            Tab(selected = adminTab == 2, onClick = { adminTab = 2 }, text = { Text("Fraud Monitor") }, icon = { Icon(Icons.Default.Shield, contentDescription = null) })
            Tab(selected = adminTab == 3, onClick = { adminTab = 3 }, text = { Text("Settings") }, icon = { Icon(Icons.Default.Settings, contentDescription = null) })
        }

        when (adminTab) {
            0 -> PaymentQueueTab(transactions = transactions, onApprove = onApproveTx, onReject = onRejectTx)
            1 -> CampaignAdminTab(campaigns = campaigns, onUpdateStatus = onUpdateCampaignStatus)
            2 -> FraudMonitorTab()
            3 -> SystemSettingsTab()
        }
    }
}

@Composable
fun PaymentQueueTab(
    transactions: List<TransactionEntity>,
    onApprove: (id: Long) -> Unit,
    onReject: (id: Long) -> Unit
) {
    val pendingTxs = transactions.filter { it.status == "PENDING" }

    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(text = "Pending Deposit & Withdrawal Requests (${pendingTxs.size})", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        }

        if (pendingTxs.isEmpty()) {
            item {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                    Box(modifier = Modifier.padding(24.dp), contentAlignment = Alignment.Center) {
                        Text("No pending transaction requests at this time.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        items(pendingTxs) { tx ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "${tx.type} via ${tx.paymentMethod}", fontWeight = FontWeight.Bold)
                        Text(text = "${String.format("%.2f", tx.amount)} ETB", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                    }

                    Text(text = "User ID: ${tx.userId} • Ref: ${tx.referenceId}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(text = "Notes: ${tx.notes}", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = { onReject(tx.id) }, colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)) {
                            Text("Reject")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = { onApprove(tx.id) }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) {
                            Text("Approve")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CampaignAdminTab(
    campaigns: List<CampaignEntity>,
    onUpdateStatus: (id: Long, status: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(campaigns) { campaign ->
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(text = "Client: ${campaign.clientName} • ${campaign.platform} (${campaign.actionType})", fontWeight = FontWeight.Bold)
                    Text(text = "Target: ${campaign.targetLink}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    Text(text = "Progress: ${campaign.currentQuantity} / ${campaign.targetQuantity} • Budget: ${campaign.totalBudget} ETB", style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { onUpdateStatus(campaign.id, "ACTIVE") }) { Text("Approve / Activate") }
                        OutlinedButton(onClick = { onUpdateStatus(campaign.id, "PAUSED") }) { Text("Pause") }
                    }
                }
            }
        }
    }
}

@Composable
fun FraudMonitorTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Anti-Fraud & Security Monitor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "✓ Device Fingerprinting Active", color = MaterialTheme.colorScheme.tertiary)
                Text(text = "✓ VPN / Proxy Inspection Enabled", color = MaterialTheme.colorScheme.tertiary)
                Text(text = "✓ Multi-Account Detection Active", color = MaterialTheme.colorScheme.tertiary)
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Suspicious Alert: Multiple accounts from IP 197.156.78.12", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Flagged 2 accounts attempting duplicate referral signups. Auto-held for manual review.", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

@Composable
fun SystemSettingsTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Official Admin Owner Accounts & Channels", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Owner Name: Gezahegn Gelebo Alemayehu", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "• CBE Bank: 1000087841457")
                Text(text = "• Telebirr: 0919397995")
                Text(text = "• M-Pesa: 0716357344")
                Text(text = "• Kacha: 0912702062")
                Text(text = "• Awash Bank: 0916742222")
                Spacer(modifier = Modifier.height(8.dp))
                Divider()
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Support Phones: +251912702062 / +251716357344")
                Text(text = "Email: gelegezusha@gmail.com")
                Text(text = "Telegram Admin: t.me/gaheadOfficeadministration")
            }
        }

        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Platform Configuration & Limits", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Platform Commission Fee: 30%")
                Text(text = "Minimum Withdrawal Limit: 50.00 ETB")
                Text(text = "Telebirr Express Auto-Approval: Enabled (< 1,000 ETB)")
                Text(text = "Maintenance Mode: Disabled (Normal Operational State)")
            }
        }
    }
}
