package com.example.gringotts_expensecalculator

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class Transaction (

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val sender: String,
    val rawMessage: String,
    val amount: Double,
    val merchant: String?,
    val type: String,
    val timestamp: Long = System.currentTimeMillis()
)