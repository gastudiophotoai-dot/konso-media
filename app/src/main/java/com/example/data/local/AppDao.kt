package com.example.data.local

import androidx.room.*
import com.example.data.model.CampaignEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateUser(user: UserEntity)

    @Query("UPDATE users SET balance = balance + :amount WHERE id = :userId")
    suspend fun addBalance(userId: String, amount: Double)

    @Query("UPDATE users SET balance = balance - :amount WHERE id = :userId")
    suspend fun deductBalance(userId: String, amount: Double)
}

@Dao
interface TransactionDao {
    @Query("SELECT * FROM wallet_transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM wallet_transactions WHERE userId = :userId ORDER BY timestamp DESC")
    fun getTransactionsForUser(userId: String): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("UPDATE wallet_transactions SET status = :status WHERE id = :transactionId")
    suspend fun updateTransactionStatus(transactionId: Long, status: String)
}

@Dao
interface CampaignDao {
    @Query("SELECT * FROM campaigns ORDER BY createdAt DESC")
    fun getAllCampaigns(): Flow<List<CampaignEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCampaign(campaign: CampaignEntity)

    @Query("UPDATE campaigns SET currentQuantity = currentQuantity + :count WHERE id = :campaignId")
    suspend fun incrementCampaignProgress(campaignId: Long, count: Int)

    @Query("UPDATE campaigns SET status = :status WHERE id = :campaignId")
    suspend fun updateCampaignStatus(campaignId: Long, status: String)
}

@Dao
interface NotificationDao {
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markAsRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllAsRead()
}
