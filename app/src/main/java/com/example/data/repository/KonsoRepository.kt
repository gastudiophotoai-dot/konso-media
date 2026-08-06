package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.*
import kotlinx.coroutines.flow.Flow

class KonsoRepository(private val database: KonsoDatabase) {

    private val userDao = database.userDao()
    private val transactionDao = database.transactionDao()
    private val campaignDao = database.campaignDao()
    private val notificationDao = database.notificationDao()

    fun getUser(userId: String): Flow<UserEntity?> = userDao.getUserById(userId)
    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
    fun getUserTransactions(userId: String): Flow<List<TransactionEntity>> = transactionDao.getTransactionsForUser(userId)
    fun getAllCampaigns(): Flow<List<CampaignEntity>> = campaignDao.getAllCampaigns()
    fun getAllNotifications(): Flow<List<NotificationEntity>> = notificationDao.getAllNotifications()

    suspend fun createOrUpdateUser(user: UserEntity) = userDao.insertOrUpdateUser(user)

    suspend fun addTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
        if (transaction.status == "COMPLETED") {
            if (transaction.type == "DEPOSIT" || transaction.type == "CAMPAIGN_REWARD" || transaction.type == "REFERRAL_BONUS") {
                userDao.addBalance(transaction.userId, transaction.amount)
            } else if (transaction.type == "WITHDRAWAL" || transaction.type == "CAMPAIGN_PAYMENT") {
                userDao.deductBalance(transaction.userId, transaction.amount + transaction.fee)
            }
        }
    }

    suspend fun updateTransactionStatus(id: Long, status: String) {
        transactionDao.updateTransactionStatus(id, status)
    }

    suspend fun createCampaign(campaign: CampaignEntity, userId: String) {
        campaignDao.insertCampaign(campaign)
        // Deduct payment
        addTransaction(
            TransactionEntity(
                userId = userId,
                type = "CAMPAIGN_PAYMENT",
                paymentMethod = "Wallet",
                amount = campaign.totalBudget,
                fee = 0.0,
                status = "COMPLETED",
                referenceId = "CMP-${System.currentTimeMillis() % 100000}",
                notes = "Campaign for ${campaign.platform} (${campaign.actionType})"
            )
        )
    }

    suspend fun completeTask(campaignId: Long, userId: String, rewardAmount: Double) {
        campaignDao.incrementCampaignProgress(campaignId, 1)
        addTransaction(
            TransactionEntity(
                userId = userId,
                type = "CAMPAIGN_REWARD",
                paymentMethod = "Wallet",
                amount = rewardAmount,
                fee = 0.0,
                status = "COMPLETED",
                referenceId = "REW-${System.currentTimeMillis() % 100000}",
                notes = "Reward for engaging with campaign #$campaignId"
            )
        )
        notificationDao.insertNotification(
            NotificationEntity(
                title = "Task Reward Received! 💰",
                message = "You earned +$rewardAmount ETB for completing a social engagement task.",
                type = "WALLET"
            )
        )
    }

    suspend fun updateCampaignStatus(campaignId: Long, status: String) {
        campaignDao.updateCampaignStatus(campaignId, status)
    }

    suspend fun addNotification(notification: NotificationEntity) {
        notificationDao.insertNotification(notification)
    }

    suspend fun markNotificationAsRead(id: Long) {
        notificationDao.markAsRead(id)
    }

    suspend fun markAllNotificationsAsRead() {
        notificationDao.markAllAsRead()
    }

    suspend fun seedInitialDataIfEmpty() {
        // Pre-populate realistic data if empty
        val defaultUser = UserEntity(
            id = "USR-001",
            name = "Abebe Kebede",
            email = "abebe@konsomedia.com",
            phone = "+251 91 123 4567",
            role = "USER",
            balance = 485.50,
            totalEarned = 1250.00,
            totalSpent = 350.00,
            referralCode = "KONSO2026",
            referredCount = 12,
            referralEarnings = 180.00
        )
        userDao.insertOrUpdateUser(defaultUser)

        val initialCampaigns = listOf(
            CampaignEntity(
                clientName = "Habesha Tech",
                platform = "TikTok",
                actionType = "Followers",
                targetLink = "https://tiktok.com/@habeshatech",
                targetQuantity = 1000,
                currentQuantity = 742,
                pricePerUnit = 1.20,
                totalBudget = 1200.00,
                targetCountry = "Ethiopia",
                status = "ACTIVE"
            ),
            CampaignEntity(
                clientName = "Addis Boutique",
                platform = "Instagram",
                actionType = "Likes",
                targetLink = "https://instagram.com/p/addis_fashion",
                targetQuantity = 500,
                currentQuantity = 389,
                pricePerUnit = 0.80,
                totalBudget = 400.00,
                targetCountry = "Ethiopia",
                status = "ACTIVE"
            ),
            CampaignEntity(
                clientName = "Ethio Music Pulse",
                platform = "YouTube",
                actionType = "Views",
                targetLink = "https://youtube.com/watch?v=konso_vibes",
                targetQuantity = 2000,
                currentQuantity = 1520,
                pricePerUnit = 0.50,
                totalBudget = 1000.00,
                targetCountry = "East Africa",
                status = "ACTIVE"
            ),
            CampaignEntity(
                clientName = "East Africa Travel",
                platform = "Telegram",
                actionType = "Followers",
                targetLink = "https://t.me/eastafricatravel",
                targetQuantity = 1500,
                currentQuantity = 1500,
                pricePerUnit = 1.00,
                totalBudget = 1500.00,
                targetCountry = "Kenya",
                status = "COMPLETED"
            )
        )
        for (cmp in initialCampaigns) {
            campaignDao.insertCampaign(cmp)
        }

        val initialTransactions = listOf(
            TransactionEntity(
                userId = "USR-001",
                type = "DEPOSIT",
                paymentMethod = "Telebirr",
                amount = 500.00,
                status = "COMPLETED",
                referenceId = "TLB-9847210",
                notes = "Telebirr Express Deposit"
            ),
            TransactionEntity(
                userId = "USR-001",
                type = "WITHDRAWAL",
                paymentMethod = "CBE Birr",
                amount = 200.00,
                fee = 5.00,
                status = "COMPLETED",
                referenceId = "CBE-3321908",
                notes = "Withdrawal to CBE 100029384728"
            ),
            TransactionEntity(
                userId = "USR-001",
                type = "REFERRAL_BONUS",
                paymentMethod = "Wallet",
                amount = 35.00,
                status = "COMPLETED",
                referenceId = "REF-202608",
                notes = "Bonus for referred user signup"
            )
        )
        for (tx in initialTransactions) {
            transactionDao.insertTransaction(tx)
        }

        val initialNotifications = listOf(
            NotificationEntity(
                title = "Welcome to Konso Media! 🚀",
                message = "Your official organic media growth & wallet platform is fully active.",
                type = "SYSTEM"
            ),
            NotificationEntity(
                title = "Telebirr Deposit Approved",
                message = "500.00 ETB has been credited to your main balance.",
                type = "WALLET"
            ),
            NotificationEntity(
                title = "Security Check Passed ✅",
                message = "Device fingerprint verified. Device is marked as trusted.",
                type = "SECURITY"
            )
        )
        for (notif in initialNotifications) {
            notificationDao.insertNotification(notif)
        }
    }
}
