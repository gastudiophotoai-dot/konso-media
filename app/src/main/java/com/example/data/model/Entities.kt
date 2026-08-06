package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val role: String = "USER", // "USER", "CLIENT", "ADMIN"
    val balance: Double = 250.00, // ETB starter balance
    val totalEarned: Double = 450.00,
    val totalSpent: Double = 120.00,
    val referralCode: String = "KONSO2026",
    val referredCount: Int = 5,
    val referralEarnings: Double = 75.00,
    val isKycVerified: Boolean = true,
    val isDeviceTrusted: Boolean = true,
    val vpnDetected: Boolean = false,
    val createdDate: String = "2026-08-01"
)

@Entity(tableName = "wallet_transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val type: String, // "DEPOSIT", "WITHDRAWAL", "CAMPAIGN_REWARD", "CAMPAIGN_PAYMENT", "REFERRAL_BONUS"
    val paymentMethod: String, // "Telebirr", "CBE Birr", "M-PESA", "Bank Transfer", "Wallet"
    val amount: Double,
    val fee: Double = 0.0,
    val status: String, // "COMPLETED", "PENDING", "PROCESSING", "REJECTED"
    val referenceId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val notes: String = ""
)

@Entity(tableName = "campaigns")
data class CampaignEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val clientName: String,
    val platform: String, // "TikTok", "Instagram", "Telegram", "Facebook", "YouTube"
    val actionType: String, // "Followers", "Likes", "Views", "Shares"
    val targetLink: String,
    val targetQuantity: Int,
    val currentQuantity: Int = 0,
    val pricePerUnit: Double,
    val totalBudget: Double,
    val targetCountry: String, // "Ethiopia", "Kenya", "USA", "Global", "East Africa"
    val status: String = "ACTIVE", // "ACTIVE", "PAUSED", "COMPLETED", "PENDING_REVIEW"
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: String, // "SYSTEM", "WALLET", "CAMPAIGN", "SECURITY"
    val isRead: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class TestimonialItem(
    val name: String,
    val role: String,
    val rating: Float,
    val comment: String,
    val avatarUrl: String = ""
)

data class FaqItem(
    val question: String,
    val questionAmharic: String,
    val answer: String,
    val answerAmharic: String,
    val category: String
)
