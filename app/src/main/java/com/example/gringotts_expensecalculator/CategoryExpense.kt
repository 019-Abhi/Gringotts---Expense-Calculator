package com.example.gringotts_expensecalculator

data class CategoryExpense(
    val category: String,
    val totalAmount: Double,
    val transactionCount: Int
)
