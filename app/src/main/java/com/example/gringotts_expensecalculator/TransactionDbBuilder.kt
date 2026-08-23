package com.example.gringotts_expensecalculator

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class TransactionDbBuilder {

    fun getDatabase(context: Context): TransactionDatabase{
        return database ?: synchronized(this) {
            database ?: Room.databaseBuilder(
                context.applicationContext,
                TransactionDatabase::class.java,
                "transaction_database"
            ).addMigrations(MIGRATION_1_2).build().also { database = it }
        }
    }

    private companion object {
        @Volatile private var database: TransactionDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE transactions ADD COLUMN category TEXT NOT NULL DEFAULT 'Uncategorized'")
            }
        }
    }

}
