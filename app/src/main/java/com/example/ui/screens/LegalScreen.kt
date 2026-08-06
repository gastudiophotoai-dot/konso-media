package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun LegalScreen(isAmharic: Boolean) {
    var legalTab by remember { mutableStateOf(0) } // 0: Privacy, 1: TOS, 2: Refund, 3: Cookie, 4: AML/KYC, 5: Social Media Terms Compliance

    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag("legal_screen")
    ) {
        ScrollableTabRow(selectedTabIndex = legalTab) {
            Tab(selected = legalTab == 0, onClick = { legalTab = 0 }, text = { Text("Privacy Policy") })
            Tab(selected = legalTab == 1, onClick = { legalTab = 1 }, text = { Text("Terms of Service") })
            Tab(selected = legalTab == 2, onClick = { legalTab = 2 }, text = { Text("Refund Policy") })
            Tab(selected = legalTab == 3, onClick = { legalTab = 3 }, text = { Text("Cookie Policy") })
            Tab(selected = legalTab == 4, onClick = { legalTab = 4 }, text = { Text("AML / KYC Policy") })
            Tab(selected = legalTab == 5, onClick = { legalTab = 5 }, text = { Text("Social Terms Alignment") })
        }

        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            when (legalTab) {
                0 -> item { PrivacyPolicyContent(isAmharic) }
                1 -> item { TermsOfServiceContent(isAmharic) }
                2 -> item { RefundPolicyContent(isAmharic) }
                3 -> item { CookiePolicyContent(isAmharic) }
                4 -> item { AmlKycPolicyContent(isAmharic) }
                5 -> item { SocialTermsComplianceContent(isAmharic) }
            }
        }
    }
}

@Composable
fun PrivacyPolicyContent(isAmharic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Privacy Policy (የግላዊነት ፖሊሲ)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "1. Data Collection: Konso Media collects phone numbers, transaction reference IDs, and device telemetry solely for processing payouts and maintaining platform integrity.\n\n" +
                        "2. Data Security: Encrypted storage with SSL/TLS transport. Personal data is never sold to third parties.\n\n" +
                        "3. User Rights: Users can request account deletion or data exports at any time via support.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TermsOfServiceContent(isAmharic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Terms of Service (የአገልግሎት ውል)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "1. Account Ownership: Each user is limited to one primary account. Multiple accounts per hardware device are strictly prohibited.\n\n" +
                        "2. Organic Engagement: Earners must voluntarily choose to follow or like accounts. Any automated bots or click scripts will result in permanent account suspension.\n\n" +
                        "3. Payout Processing: Payouts are subject to 2FA security verification.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun RefundPolicyContent(isAmharic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Refund Policy (የተመላሽ ፖሊሲ)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "1. Campaign Refunds: Unspent campaign budgets for paused or cancelled campaigns can be instantly refunded back to the main wallet balance.\n\n" +
                        "2. Delivered Services: Fully delivered followers or likes cannot be reversed or refunded once completed by real users.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun CookiePolicyContent(isAmharic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Cookie Policy (የኩኪ ፖሊሲ)", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Konso Media uses secure session tokens and device identifiers to maintain user login states and prevent unauthorized access.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun AmlKycPolicyContent(isAmharic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "AML / KYC Compliance Policy", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "To prevent Money Laundering and financial fraud, high-volume withdrawals (> 5,000 ETB) require Ethiopian National ID (Fayda) or Passport verification.",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(14.dp))

            Surface(color = MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(12.dp)) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "KYC Status: Verified (Abebe Kebede)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SocialTermsComplianceContent(isAmharic: Boolean) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Gavel, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimaryContainer)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Social Media Platform Alignment Disclosure",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Platforms like TikTok, Instagram, Facebook, and YouTube enforce anti-spam rules prohibiting automated bot traffic and fake account manipulation.\n\n" +
                        "Konso Media strictly complies with these guidelines by functioning exclusively as an organic human community hub where real individuals choose to interact voluntarily with creators.\n\n" +
                        "• Zero Bots or Automated Scripts\n" +
                        "• Real User Accounts with Active Telemetry\n" +
                        "• Voluntary Community Engagement Model",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}
