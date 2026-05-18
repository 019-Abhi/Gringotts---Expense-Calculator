package com.example.gringotts_expensecalculator

import android.content.Context
import androidx.room.Room

class TransactionDbBuilder {

    private var database: TransactionDatabase? = null

    fun getDatabase(context: Context): TransactionDatabase{

        if(database == null){
            database = Room.databaseBuilder(
                context.applicationContext,
                TransactionDatabase::class.java,
                "transaction_database"
            ).build()
        }
        return database!!
    }

}