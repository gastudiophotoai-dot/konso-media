package com.example.ui.screens

import androidx.compose.foundation.layout.*
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

@Composable
fun SecurityScreen(
    isVpnDetected: Boolean,
    isDeviceTrusted: Boolean,
    isAmharic: Boolean,
    onToggleVpn: () -> Unit,
    onTestCaptcha: () -> Unit,
    onTestOtp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .testTag("security_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Main Security Shield Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (isAmharic) "የደህንነት ሲስተም (Security Center)" else "Security & Anti-Fraud Center",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isAmharic) "OTP፣ CAPTCHA፣ VPN detection እና የመሳሪያ ጥበቃ" else "Multi-layer protection for user wallets & campaigns",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Live Device & Fingerprint Status
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAmharic) "የመሳሪያ ሁኔታ (Device Detection)" else "Device Detection & Hardware Integrity",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "Fingerprint: Android 14 (Samsung S24 Ultra)", fontWeight = FontWeight.Bold)
                        Text(text = "SIM Carrier: Ethio Telecom / Safaricom", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    Surface(
                        color = if (isDeviceTrusted) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.errorContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = if (isDeviceTrusted) "TRUSTED ✅" else "UNTRUSTED ⚠️",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // VPN / Proxy Inspection Card
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
                    Column {
                        Text(
                            text = if (isAmharic) "VPN Detection" else "VPN / Proxy Inspector",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (isVpnDetected) "VPN Detected! High Risk IP Routing." else "Clean Direct Connection",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isVpnDetected) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Switch(
                        checked = isVpnDetected,
                        onCheckedChange = { onToggleVpn() },
                        modifier = Modifier.testTag("vpn_toggle_switch")
                    )
                }
            }
        }

        // Interactive Security Simulation Buttons
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAmharic) "የደህንነት ሙከራዎች (Security Test Tools)" else "Interactive Verification Safeguards",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Button(
                        onClick = onTestCaptcha,
                        modifier = Modifier.weight(1f).testTag("test_captcha_btn")
                    ) {
                        Icon(Icons.Default.Pin, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test CAPTCHA")
                    }

                    Button(
                        onClick = onTestOtp,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                        modifier = Modifier.weight(1f).testTag("test_otp_btn")
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Test 2FA OTP")
                    }
                }
            }
        }

        // Multi-Account Detection Log
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAmharic) "Multi-Account & Suspicious Alerts" else "Multi-Account Protection",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "• Unique device UUID bound to primary account.\n• Attempting to create duplicate accounts on same hardware will lock withdrawals.\n• 2FA OTP required for all payout changes.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
