package com.example.gringotts_expensecalculator

data class Transaction (
    val sender: String,
    val rawMessage: String,
    val amount: Double,
    val merchant: String?,
    val type: String,
    val timestamp: Long = System.currentTimeMillis()
)