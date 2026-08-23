package com.example.gringotts_expensecalculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getTransactions(): List<Transaction>

    @Query("SELECT category, SUM(amount) AS totalAmount, COUNT(*) AS transactionCount FROM transactions WHERE type = 'debit' AND timestamp >= :startTime GROUP BY category ORDER BY totalAmount DESC")
    suspend fun getExpenseTotalsByCategory(startTime: Long): List<CategoryExpense>

}
