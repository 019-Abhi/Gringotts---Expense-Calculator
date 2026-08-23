package com.example.gringotts_expensecalculator

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Transaction::class],
    version = 2
)

abstract class TransactionDatabase : RoomDatabase() {
    abstract val dao: TransactionDAO
}
