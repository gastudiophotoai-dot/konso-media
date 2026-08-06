package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val password: String = "password123",
    val role: String = "USER", // "USER", "CLIENT", "ADMIN"
    val balance: Double = 250.00,
    val totalEarned: Double = 450.00,
    val totalSpent: Double = 0.00,
    val referralCode: String = "KONSO2025",
    val referredCount: Int = 0,
    val referralEarnings: Double = 0.0
)
