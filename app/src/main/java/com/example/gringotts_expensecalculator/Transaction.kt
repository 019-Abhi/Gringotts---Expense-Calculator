package com.example.gringotts_expensecalculator

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "transactions")
data class Transaction (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val sender: String,
    val rawMessage: String,
    val amount: Double,
    val merchant: String?,
    val merchantKey: String = "unknown",
    val type: String,
    val category: String = "Uncategorized",
    val source: String = "SMS",
    val timestamp: Long = System.currentTimeMillis()
)
