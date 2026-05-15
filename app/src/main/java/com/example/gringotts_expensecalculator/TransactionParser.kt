package com.example.gringotts_expensecalculator

object TransactionParser {

    private val amountRegex = Regex(
        """(?:Rs\.?|INR|₹)\s*([\d,]+(?:\.\d{1,2})?)""",
        RegexOption.IGNORE_CASE
    )

    private val debitRegex = Regex(
        """debited|debit|spent|paid|payment of|charged|withdrawn""",
        RegexOption.IGNORE_CASE
    )
    private val CreditRegex = Regex(
        """credited|credit|received|deposited""",
        RegexOption.IGNORE_CASE
    )

    private val merchantRegex = Regex(
        """(?:at|to|by)\s+([A-Za-z0-9 &\-]{2,40})""",
        RegexOption.IGNORE_CASE
    )

    fun parse(sender: String, rawMessage: String): Transaction? {
        val amountMatch = amountRegex.find(rawMessage) ?: return null
        val amountStr = amountMatch.groupValues[1].replace(",", "")
        val amount = amountStr.toDoubleOrNull() ?: return null

        val type = when{
            debitRegex.containsMatchIn(rawMessage) -> "debit"
            CreditRegex.containsMatchIn(rawMessage) -> "credit"
            else -> return null
        }

        val merchant = merchantRegex.find(rawMessage)?.groupValues?.get(1)?.trim()

        return Transaction(
            sender = sender,
            rawMessage = rawMessage,
            amount = amount,
            merchant = merchant,
            type = type
        )
    }

}