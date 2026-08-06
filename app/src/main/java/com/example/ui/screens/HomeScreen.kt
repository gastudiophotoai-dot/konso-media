package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.FaqItem
import com.example.data.model.TestimonialItem
import com.example.ui.theme.KonsoGold

@Composable
fun HomeScreen(
    isAmharic: Boolean,
    onNavigateToDashboard: () -> Unit,
    onNavigateToCampaigns: () -> Unit
) {
    val testimonials = remember {
        listOf(
            TestimonialItem(
                name = "Dawit Tadesse",
                role = "TikTok Creator (50K+)",
                rating = 5.0f,
                comment = "Konso Media helped me grow my organic follower base safely in Ethiopia. Telebirr withdrawal is super fast!"
            ),
            TestimonialItem(
                name = "Saron Worku",
                role = "Digital Marketer",
                rating = 4.9f,
                comment = "Great platform for launching targeted campaigns in East Africa. Clear metrics and reliable delivery."
            ),
            TestimonialItem(
                name = "Ephrem Kassa",
                role = "Daily Earner",
                rating = 5.0f,
                comment = "I earn extra income every day by engaging with verified channels. Instant payout to CBE Birr!"
            )
        )
    }

    val faqs = remember {
        listOf(
            FaqItem(
                question = "How does Konso Media work?",
                questionAmharic = "ኮንሶ ሚዲያ እንዴት ይሰራል?",
                answer = "Konso Media connects advertisers seeking organic social media engagement with real users who complete interaction tasks for monetary rewards.",
                answerAmharic = "ኮንሶ ሚዲያ በህጋዊ መንገድ የሶሻል ሚዲያ ተከታዮችን ማሳደግ ለሚፈልጉ ደንበኞች እና ስራ በመስራት ገቢ ማግኘት ለሚፈልጉ ተጠቃሚዎች ማገናኛ መድረክ ነው።",
                category = "General"
            ),
            FaqItem(
                question = "Is it compliant with TikTok, Instagram & Facebook terms?",
                questionAmharic = "ከሶሻል ሚዲያ ህጎች ጋር ይስማማል?",
                answer = "Yes! All interactions on Konso Media are driven by real, voluntary human users. We strictly prohibit bots, click farms, and automated scripts to ensure 100% compliance.",
                answerAmharic = "አዎ! ሁሉም ተከታዮች እና ላይኮች በእውነተኛ ሰዎች በፍቃደኝነት የሚደረጉ በመሆናቸው ከህግ ጋር ሙሉ በሙሉ የሚጣጣም ነው።",
                category = "Compliance"
            ),
            FaqItem(
                question = "Which payment methods are supported in Ethiopia?",
                questionAmharic = "የትኞቹ የክፍያ አማራጮች ይሰራሉ?",
                answer = "We support Telebirr, CBE (Commercial Bank of Ethiopia), CBE Birr, M-PESA, and Direct Bank Transfers.",
                answerAmharic = "ቴሌብር (Telebirr)፣ የኢትዮጵያ ንግድ ባንክ (CBE)፣ ሲቢኢ ብር (CBE Birr)፣ ኤም-ፔሳ (M-PESA) እና የባንክ ሂሳብ ማስተላለፍ እንቀበላለን።",
                category = "Payments"
            ),
            FaqItem(
                question = "How fast are withdrawals processed?",
                questionAmharic = "ገንዘብ ለማውጣት ምን ያህል ጊዜ ይወስዳል?",
                answer = "Telebirr and CBE Birr instant withdrawals take 1 to 15 minutes after automated security screening.",
                answerAmharic = "በቴሌብር እና በሲቢኢ ብር የሚደረጉ የገንዘብ ማውጣት ጥያቄዎች ከ1 እስከ 15 ደቂቃ ባለው ጊዜ ውስጥ ይፈጸማሉ።",
                category = "Withdrawals"
            )
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen_lazy_column"),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        // Hero Section with Banner
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                // Background Banner Image
                Image(
                    painter = painterResource(id = R.drawable.konso_hero_banner_1785918105420),
                    contentDescription = "Hero Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Dark Gradient Overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.85f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = if (isAmharic) "ኦፊሴላዊ የሶሻል ሚዲያ ማሳደጊያ መድረክ v1.0" else "Official Organic Growth Platform v1.0",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (isAmharic) "Konso Media Follower & Liker" else "Konso Media Follower & Liker",
                        style = MaterialTheme.typography.displayLarge,
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 26.sp
                    )

                    Text(
                        text = if (isAmharic)
                            "በእውነተኛ ሰዎች አማካኝነት የሶሻል ሚዲያ ገጾችዎን ያሳድጉ፤ ወይም በስራዎች ተሳታፊ በመሆን ገቢ ያግኙ!"
                        else
                            "Empowering creators and businesses with organic followers, likes & rewards across East Africa.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.9f)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = onNavigateToDashboard,
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                            modifier = Modifier.testTag("home_start_earning_btn")
                        ) {
                            Icon(Icons.Default.MonetizationOn, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isAmharic) "ገቢ ማግኘት ጀምር" else "Start Earning")
                        }

                        OutlinedButton(
                            onClick = onNavigateToCampaigns,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                            modifier = Modifier.testTag("home_create_campaign_btn")
                        ) {
                            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = if (isAmharic) "ዘመቻ ክፈት" else "Create Campaign")
                        }
                    }
                }
            }
        }

        // Live Statistics Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isAmharic) "የቅርብ ጊዜ ስታቲስቲክስ (Live Platform Stats)" else "Live Platform Statistics",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatBox("58.4K+", if (isAmharic) "ተጠቃሚዎች" else "Active Users", Icons.Default.People)
                        StatBox("1.2M+", if (isAmharic) "የተላኩ Follows" else "Delivered", Icons.Default.ThumbUp)
                        StatBox("ETB 850K+", if (isAmharic) "የተከፈለ ገቢ" else "Payouts", Icons.Default.AccountBalanceWallet)
                    }
                }
            }
        }

        // How It Works Section
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Text(
                    text = if (isAmharic) "እንዴት እንደሚሰራ (How It Works)" else "How Konso Media Works",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = if (isAmharic) "በ3 ቀላል ደረጃዎች ይጀምሩ" else "3 simple steps to grow or earn",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                WorkflowCard(
                    stepNumber = "1",
                    title = if (isAmharic) "መዝገብ እና OTP ያረጋግጡ" else "Register & Verify OTP",
                    description = if (isAmharic) "በስልክ ቁጥር ወይም ኢሜይል ተመዝግበው ባለ2-ደረጃ ማረጋገጫ ያጠናቅቁ።" else "Sign up securely using your phone or email with 2FA security.",
                    icon = Icons.Default.Lock
                )

                WorkflowCard(
                    stepNumber = "2",
                    title = if (isAmharic) "ዘመቻ ይክፈቱ ወይም ስራ ይሰሩ" else "Create Campaign or Complete Tasks",
                    description = if (isAmharic) "ለTikTok፣ Instagram፣ Telegram ዘመቻ ይክፈቱ ወይም ላይክ እና ፎሎው በማድረግ ገቢ ያግኙ።" else "Launch social campaigns for TikTok/Instagram or engage to earn rewards.",
                    icon = Icons.Default.AddTask
                )

                WorkflowCard(
                    stepNumber = "3",
                    title = if (isAmharic) "በTelebirr / CBE ወዲያውኑ ይውጡ" else "Instant Payout via Telebirr / CBE",
                    description = if (isAmharic) "ያገኙትን ገቢ በቴሌብር፣ በሲቢኢ ብር ወይም በባንክ ወዲያውኑ ያውጡ።" else "Withdraw your earnings directly to Telebirr, CBE Birr or Bank.",
                    icon = Icons.Default.Payments
                )
            }
        }

        // Testimonials / User Reviews
        item {
            Column(modifier = Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = if (isAmharic) "የተጠቃሚዎች ግምገማ (User Reviews)" else "What Our Users Say",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(testimonials) { item ->
                        TestimonialCard(item)
                    }
                }
            }
        }

        // Official Payment & Contact Card Section
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountBalance, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isAmharic) "የአስተዳደር የባንክ ሂሳቦች እና መገናኛ" else "Official Payment Accounts & Contact",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Account Owner: Gezahegn Gelebo Alemayehu",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Bank & Wallet Account Chips Grid
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "CBE (ንግድ ባንክ)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "1000087841457", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                }
                            }
                            Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "Telebirr (ቴሌብር)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "0919397995", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                }
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "M-Pesa (ኤም-ፔሳ)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "0716357344", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                }
                            }
                            Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), modifier = Modifier.weight(1f)) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Text(text = "Kacha (ካቻ)", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text(text = "0912702062", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                }
                            }
                        }

                        Surface(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Awash Bank (አዋሽ ባንክ):", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                                Text(text = "0916742222", fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Support Contacts
                    Text(
                        text = if (isAmharic) "እኛን ለማግኘት (Contact Support):" else "Official Support & Contact Channels:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Phone, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "+251912702062 / +251716357344", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Email, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "gelegezusha@gmail.com", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Send, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "t.me/gaheadOfficeadministration", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }

        // FAQ Section (Accordion)
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = if (isAmharic) "ተደጋጋሚ ጥያቄዎች (FAQ)" else "Frequently Asked Questions",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                faqs.forEach { faq ->
                    FaqAccordionItem(faq = faq, isAmharic = isAmharic)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun StatBox(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(28.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold)
        Text(text = label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun WorkflowCard(stepNumber: String, title: String, description: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = CircleShape,
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = stepNumber, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(text = description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun TestimonialCard(item: TestimonialItem) {
    Card(
        modifier = Modifier
            .width(260.dp)
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(32.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = item.name, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyMedium)
                    Text(text = item.role, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                repeat(5) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = KonsoGold, modifier = Modifier.size(14.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = item.comment,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun FaqAccordionItem(faq: FaqItem, isAmharic: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isAmharic) faq.questionAmharic else faq.question,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }

            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(8.dp))
                    Divider(color = MaterialTheme.colorScheme.surfaceVariant)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isAmharic) faq.answerAmharic else faq.answer,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
