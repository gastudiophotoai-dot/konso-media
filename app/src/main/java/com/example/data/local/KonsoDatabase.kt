package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CampaignEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TransactionEntity::class,
        CampaignEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class KonsoDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun campaignDao(): CampaignDao
    abstract fun notificationDao(): NotificationDao

    companion object {
        @Volatile
        private var INSTANCE: KonsoDatabase? = null

        fun getDatabase(context: Context): KonsoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KonsoDatabase::class.java,
                    "konso_media_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
