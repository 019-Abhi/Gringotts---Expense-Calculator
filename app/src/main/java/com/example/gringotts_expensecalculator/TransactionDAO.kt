package com.example.gringotts_expensecalculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDAO {

    @Insert
    suspend fun insertTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    suspend fun getAllTransactions(): List<Transaction>


}