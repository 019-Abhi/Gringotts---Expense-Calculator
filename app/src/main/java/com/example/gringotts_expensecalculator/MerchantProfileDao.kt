package com.example.gringotts_expensecalculator

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MerchantProfileDao {
    @Query("SELECT * FROM merchant_profiles WHERE merchantKey = :merchantKey LIMIT 1")
    suspend fun get(merchantKey: String): MerchantProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(profile: MerchantProfile)
}
