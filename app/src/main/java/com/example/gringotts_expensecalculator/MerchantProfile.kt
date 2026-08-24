package com.example.gringotts_expensecalculator

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "merchant_profiles")
data class MerchantProfile(
    @PrimaryKey val merchantKey: String,
    val displayName: String,
    val category: String,
    val source: String,
    val updatedAt: Long = System.currentTimeMillis()
)
