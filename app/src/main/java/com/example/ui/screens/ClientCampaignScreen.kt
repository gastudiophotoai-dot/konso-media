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

@Composable
fun ClientCampaignScreen(
    campaigns: List<CampaignEntity>,
    userBalance: Double,
    isAmharic: Boolean,
    onCreateCampaign: (
        clientName: String,
        platform: String,
        actionType: String,
        link: String,
        quantity: Int,
        pricePerUnit: Double,
        country: String
    ) -> Unit,
    onPauseCampaign: (id: Long) -> Unit,
    onResumeCampaign: (id: Long) -> Unit
) {
    var showCreateModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("client_campaign_screen")
    ) {
        // Header Card with Action to Launch New Campaign
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = if (isAmharic) "የደንበኛ ዘመቻዎች (Client Campaign System)" else "Advertiser Campaign Manager",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

                Text(
                    text = if (isAmharic)
                        "TikTok, Instagram, Telegram, Facebook እና YouTube ላይ ተከታዮችን እና ላይኮችን በህጋዊ መንገድ ያሳድጉ"
                    else
                        "Boost your followers, likes, and views across TikTok, Instagram, Telegram, Facebook & YouTube.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { showCreateModal = true },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.testTag("launch_new_campaign_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isAmharic) "አዲስ ዘመቻ ፍጠር (Create Campaign)" else "Create New Campaign")
                }
            }
        }

        // Active & Historical Campaigns List
        Text(
            text = if (isAmharic) "የዘመቻ ሪፖርቶች (Campaign Reports & Status)" else "Campaign Reports & Analytics",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(campaigns) { campaign ->
                CampaignReportCard(
                    campaign = campaign,
                    isAmharic = isAmharic,
                    onPause = { onPauseCampaign(campaign.id) },
                    onResume = { onResumeCampaign(campaign.id) }
                )
            }
        }
    }

    if (showCreateModal) {
        CreateCampaignDialog(
            userBalance = userBalance,
            isAmharic = isAmharic,
            onDismiss = { showCreateModal = false },
            onCreate = { client, plat, action, link, qty, price, country ->
                showCreateModal = false
                onCreateCampaign(client, plat, action, link, qty, price, country)
            }
        )
    }
}

@Composable
fun CampaignReportCard(
    campaign: CampaignEntity,
    isAmharic: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit
) {
    val progress = if (campaign.targetQuantity > 0) campaign.currentQuantity.toFloat() / campaign.targetQuantity.toFloat() else 0f

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = campaign.platform,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = campaign.actionType,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    color = when (campaign.status) {
                        "ACTIVE" -> MaterialTheme.colorScheme.tertiaryContainer
                        "COMPLETED" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = campaign.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Target Link: ${campaign.targetLink}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1
            )

            Text(
                text = "Target Country: ${campaign.targetCountry} • Budget: ${String.format("%.2f", campaign.totalBudget)} ETB",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Delivered: ${campaign.currentQuantity} / ${campaign.targetQuantity}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (campaign.status == "ACTIVE") {
                    OutlinedButton(onClick = onPause) {
                        Icon(Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pause")
                    }
                } else if (campaign.status == "PAUSED") {
                    Button(onClick = onResume) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Resume")
                    }
                }
            }
        }
    }
}

@Composable
fun CreateCampaignDialog(
    userBalance: Double,
    isAmharic: Boolean,
    onDismiss: () -> Unit,
    onCreate: (
        clientName: String,
        platform: String,
        actionType: String,
        link: String,
        quantity: Int,
        pricePerUnit: Double,
        country: String
    ) -> Unit
) {
    var platform by remember { mutableStateOf("TikTok") }
    var actionType by remember { mutableStateOf("Followers") }
    var targetLink by remember { mutableStateOf("https://tiktok.com/@my_brand") }
    var targetQtyText by remember { mutableStateOf("1000") }
    var targetCountry by remember { mutableStateOf("Ethiopia") }
    var clientName by remember { mutableStateOf("My Brand") }

    val unitPrice = when (actionType) {
        "Followers" -> 1.20
        "Likes" -> 0.80
        "Views" -> 0.50
        else -> 1.00
    }

    val qty = targetQtyText.toIntOrNull() ?: 100
    val totalBudget = qty * unitPrice

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isAmharic) "አዲስ ዘመቻ መፍጠሪያ (Create Campaign)" else "Create Social Campaign") },
        text = {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                item {
                    Text(if (isAmharic) "የሶሻል ሚዲያ መድረክ ይምረጡ:" else "Select Platform:")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("TikTok", "Instagram", "Telegram", "YouTube", "Facebook").forEach { p ->
                            FilterChip(
                                selected = platform == p,
                                onClick = { platform = p },
                                label = { Text(p) }
                            )
                        }
                    }
                }

                item {
                    Text(if (isAmharic) "የድርጊት አይነት (Action):" else "Action Type:")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Followers", "Likes", "Views", "Shares").forEach { a ->
                            FilterChip(
                                selected = actionType == a,
                                onClick = { actionType = a },
                                label = { Text(a) }
                            )
                        }
                    }
                }

                item {
                    OutlinedTextField(
                        value = clientName,
                        onValueChange = { clientName = it },
                        label = { Text("Brand / Client Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("campaign_brand_field")
                    )
                }

                item {
                    OutlinedTextField(
                        value = targetLink,
                        onValueChange = { targetLink = it },
                        label = { Text("Social Media Target Link") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("campaign_link_field")
                    )
                }

                item {
                    OutlinedTextField(
                        value = targetQtyText,
                        onValueChange = { targetQtyText = it },
                        label = { Text("Target Quantity (e.g. 1000)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("campaign_qty_field")
                    )
                }

                item {
                    Text("Target Country:")
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        listOf("Ethiopia", "Kenya", "USA", "Global", "East Africa").forEach { c ->
                            FilterChip(
                                selected = targetCountry == c,
                                onClick = { targetCountry = c },
                                label = { Text(c) }
                            )
                        }
                    }
                }

                item {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Rate per unit: $unitPrice ETB")
                            Text("Total Budget Required: ${String.format("%.2f", totalBudget)} ETB", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            Text("Your Wallet Balance: ${String.format("%.2f", userBalance)} ETB", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onCreate(clientName, platform, actionType, targetLink, qty, unitPrice, targetCountry)
                },
                enabled = userBalance >= totalBudget,
                modifier = Modifier.testTag("confirm_create_campaign_btn")
            ) {
                Text(if (isAmharic) "ዘመቻውን ጀምር" else "Launch Campaign")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
