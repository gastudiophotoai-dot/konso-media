package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CampaignEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import com.example.ui.theme.KonsoGold

@Composable
fun UserDashboardScreen(
    user: UserEntity?,
    transactions: List<TransactionEntity>,
    activeCampaigns: List<CampaignEntity>,
    notifications: List<NotificationEntity>,
    isAmharic: Boolean,
    onDeposit: (method: String, amount: Double, ref: String) -> Unit,
    onWithdraw: (method: String, amount: Double, account: String) -> Unit,
    onCompleteTask: (campaign: CampaignEntity) -> Unit,
    onMarkNotificationsRead: () -> Unit,
    onSecurityVerificationRequest: (actionName: String) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) } // 0: Overview/Earn, 1: Wallet, 2: Referral, 3: Notifications
    var showDepositModal by remember { mutableStateOf(false) }
    var showWithdrawModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("user_dashboard_screen")
    ) {
        // Tab Header Navigation
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text(if (isAmharic) "ዳሽቦርድ / Earn" else "Dashboard") },
                icon = { Icon(Icons.Default.Dashboard, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text(if (isAmharic) "ዋሌት (Wallet)" else "Wallet") },
                icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text(if (isAmharic) "ሪፈራል (Referral)" else "Referral") },
                icon = { Icon(Icons.Default.Share, contentDescription = null) }
            )
            Tab(
                selected = selectedTab == 3,
                onClick = { selectedTab = 3 },
                text = { Text(if (isAmharic) "ማሳወቂያዎች" else "Notifications") },
                icon = {
                    BadgedBox(
                        badge = {
                            val unreadCount = notifications.count { !it.isRead }
                            if (unreadCount > 0) {
                                Badge { Text(unreadCount.toString()) }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = null)
                    }
                }
            )
        }

        when (selectedTab) {
            0 -> DashboardEarnTab(
                user = user,
                activeCampaigns = activeCampaigns,
                isAmharic = isAmharic,
                onCompleteTask = onCompleteTask,
                onDepositClick = { showDepositModal = true },
                onWithdrawClick = { showWithdrawModal = true }
            )
            1 -> WalletTab(
                user = user,
                transactions = transactions,
                isAmharic = isAmharic,
                onDepositClick = { showDepositModal = true },
                onWithdrawClick = { showWithdrawModal = true }
            )
            2 -> ReferralTab(user = user, isAmharic = isAmharic)
            3 -> NotificationsTab(notifications = notifications, isAmharic = isAmharic, onMarkRead = onMarkNotificationsRead)
        }
    }

    if (showDepositModal) {
        DepositDialog(
            isAmharic = isAmharic,
            onDismiss = { showDepositModal = false },
            onConfirmDeposit = { method, amount, ref ->
                showDepositModal = false
                onSecurityVerificationRequest("Deposit $amount ETB via $method")
                onDeposit(method, amount, ref)
            }
        )
    }

    if (showWithdrawModal) {
        WithdrawDialog(
            userBalance = user?.balance ?: 0.0,
            isAmharic = isAmharic,
            onDismiss = { showWithdrawModal = false },
            onConfirmWithdraw = { method, amount, acc ->
                showWithdrawModal = false
                onSecurityVerificationRequest("Withdraw $amount ETB to $acc")
                onWithdraw(method, amount, acc)
            }
        )
    }
}

@Composable
fun DashboardEarnTab(
    user: UserEntity?,
    activeCampaigns: List<CampaignEntity>,
    isAmharic: Boolean,
    onCompleteTask: (campaign: CampaignEntity) -> Unit,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Wallet Balance Card (Sleek Interface Gradient)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF2563EB), Color(0xFF4338CA))
                        )
                    )
                    .padding(22.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isAmharic) "የዋሌት ቀሪ ሂሳብ (Main Balance)" else "Wallet Balance",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFDBEAFE)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "${String.format("%.2f", user?.balance ?: 0.0)} ETB",
                                style = MaterialTheme.typography.displayMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        ) {
                            Icon(
                                Icons.Default.AccountBalanceWallet,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(12.dp)
                                    .size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = onDepositClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color(0xFF2563EB)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_deposit_btn")
                        ) {
                            Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isAmharic) "ገንዘብ አስገባ" else "Deposit", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = onWithdrawClick,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("dashboard_withdraw_btn")
                        ) {
                            Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isAmharic) "ገንዘብ አውጣ" else "Withdraw", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // Quick Stats Bento Grid (Sleek Style)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD1FAE5),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Color(0xFF059669), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = if (isAmharic) "አጠቃላይ የተገኘ" else "Total Earnings", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "+${String.format("%.2f", user?.totalEarned ?: 0.0)} ETB", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }

                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFFFEDD5),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Group, contentDescription = null, tint = Color(0xFFEA580C), modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = if (isAmharic) "የሪፈራል ገቢ" else "Referral Earnings", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "${String.format("%.2f", user?.referralEarnings ?: 0.0)} ETB", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }

        // Active Tasks Section (Follow & Like to Earn)
        item {
            Column {
                Text(
                    text = if (isAmharic) "የሚሰሩ ስራዎች (Available Earn Tasks)" else "Available Earn Tasks",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isAmharic) "በሶሻል ሚዲያ ተሳትፎ ወዲያውኑ ብር ያግኙ" else "Complete social media tasks to earn instant ETB rewards",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(activeCampaigns.filter { it.status == "ACTIVE" }) { campaign ->
            EarnTaskCard(campaign = campaign, isAmharic = isAmharic, onComplete = { onCompleteTask(campaign) })
        }
    }
}

@Composable
fun EarnTaskCard(campaign: CampaignEntity, isAmharic: Boolean, onComplete: () -> Unit) {
    val reward = campaign.pricePerUnit * 0.70
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("earn_task_card_${campaign.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = when (campaign.platform) {
                    "TikTok" -> Color(0xFF000000)
                    "Instagram" -> Color(0xFFE1306C)
                    "YouTube" -> Color(0xFFFF0000)
                    "Telegram" -> Color(0xFF0088CC)
                    else -> MaterialTheme.colorScheme.primary
                },
                shape = CircleShape,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = when (campaign.platform) {
                            "TikTok" -> Icons.Default.MusicNote
                            "Instagram" -> Icons.Default.CameraAlt
                            "YouTube" -> Icons.Default.PlayArrow
                            "Telegram" -> Icons.Default.Send
                            else -> Icons.Default.ThumbUp
                        },
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = "${campaign.platform} ${campaign.actionType}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "Client: ${campaign.clientName}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(
                    text = "Target Link: ${campaign.targetLink}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "+${String.format("%.2f", reward)} ETB",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.tertiary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onComplete,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier.testTag("task_complete_btn_${campaign.id}")
                ) {
                    Text(text = if (isAmharic) "ስራውን ስራ" else "Do Task", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun WalletTab(
    user: UserEntity?,
    transactions: List<TransactionEntity>,
    isAmharic: Boolean,
    onDepositClick: () -> Unit,
    onWithdrawClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = if (isAmharic) "የክፍያ አማራጮች (Supported Payment Gateways)" else "Supported Payment Methods", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Telebirr • CBE Birr • Commercial Bank of Ethiopia (CBE) • M-PESA • Bank Transfer", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = onDepositClick, modifier = Modifier.weight(1f)) {
                    Text(if (isAmharic) "ገንዘብ ማስገባት (Deposit)" else "Deposit Money")
                }
                Button(
                    onClick = onWithdrawClick,
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(if (isAmharic) "ገንዘብ ማውጣት (Withdraw)" else "Withdraw Money")
                }
            }
        }

        item {
            Text(text = if (isAmharic) "የግብይት ታሪክ (Transaction History)" else "Transaction History", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        items(transactions) { tx ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (tx.type == "DEPOSIT" || tx.type == "CAMPAIGN_REWARD") Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = if (tx.type == "DEPOSIT" || tx.type == "CAMPAIGN_REWARD") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = "${tx.type} (${tx.paymentMethod})", fontWeight = FontWeight.Bold)
                        Text(text = tx.notes, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(text = "Ref: ${tx.referenceId}", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${if (tx.type == "DEPOSIT" || tx.type == "CAMPAIGN_REWARD") "+" else "-"}${String.format("%.2f", tx.amount)} ETB",
                            fontWeight = FontWeight.Bold,
                            color = if (tx.type == "DEPOSIT" || tx.type == "CAMPAIGN_REWARD") MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error
                        )
                        Surface(
                            color = if (tx.status == "COMPLETED") MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = tx.status,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReferralTab(user: UserEntity?, isAmharic: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Share, contentDescription = null, tint = KonsoGold, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = if (isAmharic) "የእርስዎ ሪፈራል ኮድ" else "Your Personal Referral Code", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = user?.referralCode ?: "KONSO2026",
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (isAmharic)
                        "ጓደኞችዎን ይጋብዙ እና በሚሰሩት እያንዳንዱ ስራ ላይ 10% የኮሚሽን ቦነስ ያግኙ!"
                    else
                        "Invite friends and earn 10% commission on every campaign and task they complete!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${user?.referredCount ?: 0}", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    Text(text = if (isAmharic) "የተጋበዙ ሰዎች" else "Total Referred", style = MaterialTheme.typography.bodySmall)
                }
            }

            Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${String.format("%.2f", user?.referralEarnings ?: 0.0)} ETB", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = KonsoGold)
                    Text(text = if (isAmharic) "የተገኘ ኮሚሽን" else "Referral Bonus", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun NotificationsTab(
    notifications: List<NotificationEntity>,
    isAmharic: Boolean,
    onMarkRead: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = if (isAmharic) "ማሳወቂያዎች (Notifications)" else "Notifications", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            TextButton(onClick = onMarkRead) {
                Text(if (isAmharic) "ሁሉንም እንደተነበበ አድርግ" else "Mark All Read")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(notifications) { notif ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (!notif.isRead) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Text(text = notif.title, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = notif.message, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}

@Composable
fun DepositDialog(
    isAmharic: Boolean,
    onDismiss: () -> Unit,
    onConfirmDeposit: (method: String, amount: Double, ref: String) -> Unit
) {
    var method by remember { mutableStateOf("Telebirr") }
    var amountText by remember { mutableStateOf("200") }
    var refText by remember { mutableStateOf("") }

    val accountDetails = when (method) {
        "CBE Bank" -> "CBE: 1000087841457"
        "Telebirr" -> "Telebirr: 0919397995"
        "M-PESA" -> "M-Pesa: 0716357344"
        "Kacha" -> "Kacha: 0912702062"
        "Awash Bank" -> "Awash Bank: 0916742222"
        else -> "CBE: 1000087841457 | Telebirr: 0919397995"
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAmharic) "ገንዘብ ማስገቢያ (Deposit Funds)" else "Deposit Funds") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = if (isAmharic) "የክፍያ ዘዴ ይምረጡ:" else "Select Official Payment Method:",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    listOf("Telebirr", "CBE Bank", "M-PESA", "Kacha", "Awash Bank").forEach { m ->
                        FilterChip(
                            selected = method == m,
                            onClick = { method = m },
                            label = { Text(m, fontSize = 11.sp) }
                        )
                    }
                }

                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isAmharic) "የአካውንት ባለቤት (Account Name):" else "Account Owner:",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Gezahegn Gelebo Alemayehu",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = accountDetails,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Amount (ETB)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("deposit_amount_field")
                )

                OutlinedTextField(
                    value = refText,
                    onValueChange = { refText = it },
                    label = { Text(if (isAmharic) "የደረሰኝ / የትራንዛክሽን ቁጥር (Ref ID)" else "Transaction Reference ID / Receipt #") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("deposit_ref_field")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 100.0
                    onConfirmDeposit(method, amt, refText)
                },
                modifier = Modifier.testTag("confirm_deposit_btn")
            ) {
                Text(if (isAmharic) "አስገባ (Confirm Deposit)" else "Confirm Deposit")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun WithdrawDialog(
    userBalance: Double,
    isAmharic: Boolean,
    onDismiss: () -> Unit,
    onConfirmWithdraw: (method: String, amount: Double, acc: String) -> Unit
) {
    var method by remember { mutableStateOf("Telebirr") }
    var amountText by remember { mutableStateOf("150") }
    var accountText by remember { mutableStateOf("+251 91 123 4567") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAmharic) "ገንዘብ ማውጫ (Withdraw)" else "Withdraw Funds") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Available Balance: ${String.format("%.2f", userBalance)} ETB")
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Telebirr", "CBE Birr", "M-PESA", "Bank Transfer").forEach { m ->
                        FilterChip(
                            selected = method == m,
                            onClick = { method = m },
                            label = { Text(m) }
                        )
                    }
                }

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Withdraw Amount (ETB)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("withdraw_amount_field")
                )

                OutlinedTextField(
                    value = accountText,
                    onValueChange = { accountText = it },
                    label = { Text("Mobile / Account Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("withdraw_account_field")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 50.0
                    onConfirmWithdraw(method, amt, accountText)
                },
                modifier = Modifier.testTag("confirm_withdraw_btn")
            ) {
                Text(if (isAmharic) "አውጣ" else "Confirm Withdraw")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
