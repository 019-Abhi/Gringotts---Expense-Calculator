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

    @Query("SELECT * FROM transactions WHERE type = :type AND amount = :amount AND merchantKey = :merchantKey AND timestamp BETWEEN :startTime AND :endTime LIMIT 1")
    suspend fun findPossibleDuplicate(type: String, amount: Double, merchantKey: String, startTime: Long, endTime: Long): Transaction?

    @Query("UPDATE transactions SET category = :category WHERE merchantKey = :merchantKey")
    suspend fun updateCategoryForMerchant(merchantKey: String, category: String)

}
