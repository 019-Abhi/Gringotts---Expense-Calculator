package com.example.gringotts_expensecalculator

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.gringotts_expensecalculator.databinding.ActivityTransactionBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityTransactionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.bottomNavigation.selectedItemId = R.id.navigation_transactions
        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            if (it.itemId == R.id.navigation_categories) {
                startActivity(android.content.Intent(this, AnalyticsActivity::class.java))
                finish()
                true
            } else if (it.itemId == R.id.navigation_settings) {
                startActivity(android.content.Intent(this, SettingsActivity::class.java))
                finish()
                true
            } else false
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.updatePadding(top = systemBars.top)
            binding.bottomNavigation.bottomNavigation.updatePadding(bottom = systemBars.bottom)
            insets
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        loadTransactions()
    }

    private fun loadTransactions() {
        val db = TransactionDbBuilder().getDatabase(this)

        CoroutineScope(Dispatchers.IO).launch {

            val transactions = db.dao.getTransactions()

            withContext(Dispatchers.Main) {

                if(transactions.isEmpty()){
                    binding.tvEmpty.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.tvEmpty.visibility = View.GONE
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.recyclerView.adapter = TransactionAdapter(transactions, ::showCategoryPicker)
                }
            }
        }
    }

    private fun showCategoryPicker(transaction: Transaction) {
        if (transaction.merchantKey == "unknown") return
        val categories = TransactionCategoryClassifier.availableCategories.toTypedArray()
        MaterialAlertDialogBuilder(this)
            .setTitle("Categorize ${transaction.merchant}")
            .setSingleChoiceItems(categories, categories.indexOf(transaction.category)) { dialog, which ->
                CoroutineScope(Dispatchers.IO).launch {
                    TransactionIngestionRepository.changeMerchantCategory(this@TransactionsActivity, transaction, categories[which])
                    withContext(Dispatchers.Main) { dialog.dismiss(); loadTransactions() }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
