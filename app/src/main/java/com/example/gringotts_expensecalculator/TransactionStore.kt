package com.example.gringotts_expensecalculator

object TransactionStore {
    private val _transactions = mutableListOf<Transaction>()
    val transactions: List<Transaction> get() = _transactions.toList()

    fun add(transaction: Transaction) {
        _transactions.add(0, transaction)
    }
}