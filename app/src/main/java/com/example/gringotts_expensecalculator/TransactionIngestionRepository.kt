package com.example.gringotts_expensecalculator

import android.content.Context

object TransactionIngestionRepository {
    private const val DUPLICATE_WINDOW_MS = 5 * 60 * 1000L

    suspend fun ingest(context: Context, sender: String, rawMessage: String, source: String, timestamp: Long = System.currentTimeMillis()): Boolean {
        val parsed = TransactionParser.parse(sender, rawMessage, timestamp) ?: return false
        val db = TransactionDbBuilder().getDatabase(context)
        val profile = db.merchantProfileDao.get(parsed.merchantKey)
        val category = profile?.category ?: TransactionCategoryClassifier.categorize(parsed.merchant, rawMessage, parsed.type)

        if (profile == null) {
            db.merchantProfileDao.save(MerchantProfile(parsed.merchantKey, parsed.merchant ?: "Unknown merchant", category, "RULE"))
        }
        val duplicate = db.dao.findPossibleDuplicate(
            parsed.type, parsed.amount, parsed.merchantKey,
            timestamp - DUPLICATE_WINDOW_MS, timestamp + DUPLICATE_WINDOW_MS
        )
        if (duplicate != null) return false

        db.dao.insertTransaction(parsed.copy(category = category, source = source, timestamp = timestamp))
        return true
    }

    suspend fun changeMerchantCategory(context: Context, transaction: Transaction, category: String) {
        if (transaction.merchantKey == "unknown") return
        val db = TransactionDbBuilder().getDatabase(context)
        db.merchantProfileDao.save(MerchantProfile(transaction.merchantKey, transaction.merchant ?: "Unknown merchant", category, "USER"))
        db.dao.updateCategoryForMerchant(transaction.merchantKey, category)
    }
}
