package com.example.gringotts_expensecalculator

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Transaction::class, MerchantProfile::class],
    version = 3
)

abstract class TransactionDatabase : RoomDatabase() {
    abstract val dao: TransactionDAO
    abstract val merchantProfileDao: MerchantProfileDao
}
