package com.example.gringotts_expensecalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gringotts_expensecalculator.databinding.ActivityTransactionBinding

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Transactions"

        val transactions = TransactionStore.transactions

        if (transactions.isEmpty()) {
            binding.tvEmpty.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.GONE
        } else {
            binding.tvEmpty.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.recyclerView.layoutManager = LinearLayoutManager(this)
            binding.recyclerView.adapter = TransactionAdapter(transactions)
        }
    }
}