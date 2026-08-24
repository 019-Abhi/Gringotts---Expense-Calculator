package com.example.gringotts_expensecalculator

/**
 * A local, deterministic categorizer for the MVP. It can later be replaced by
 * a network-backed Gemini implementation without changing the database/UI contract.
 */
object TransactionCategoryClassifier {
    private val rules = linkedMapOf(
        "Food & Dining" to listOf("swiggy", "zomato", "restaurant", "cafe", "coffee", "pizza", "burger", "dominos", "kfc"),
        "Groceries" to listOf("blinkit", "zepto", "instamart", "bigbasket", "dmart", "supermarket", "grocery"),
        "Transport" to listOf("uber", "ola", "rapido", "metro", "irctc", "fuel", "petrol", "diesel"),
        "Bills & Utilities" to listOf("electricity", "water bill", "gas bill", "recharge", "airtel", "jio", "vi ", "broadband", "utility"),
        "Shopping" to listOf("amazon", "flipkart", "myntra", "meesho", "store", "mart"),
        "Entertainment" to listOf("netflix", "spotify", "hotstar", "prime video", "bookmyshow", "cinema"),
        "Health" to listOf("pharmacy", "apollo", "medical", "hospital", "clinic"),
        "Transfers" to listOf("upi", "transfer", "neft", "imps", "to account")
    )

    val availableCategories = rules.keys.toList() + listOf("Income", "Uncategorized")

    fun categorize(merchant: String?, rawMessage: String, type: String): String {
        if (type == "credit") return "Income"
        val searchable = "$merchant $rawMessage".lowercase()
        return rules.entries.firstOrNull { (_, keywords) -> keywords.any(searchable::contains) }
            ?.key ?: "Uncategorized"
    }
}
