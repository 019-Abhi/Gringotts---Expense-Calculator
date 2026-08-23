package com.example.gringotts_expensecalculator

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.gringotts_expensecalculator.databinding.ActivityAnalyticsBinding
import com.example.gringotts_expensecalculator.databinding.ItemCategoryBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale

class AnalyticsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnalyticsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityAnalyticsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.bottomNavigation.selectedItemId = R.id.navigation_categories
        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            if (it.itemId == R.id.navigation_transactions) {
                startActivity(android.content.Intent(this, TransactionsActivity::class.java))
                finish()
                true
            } else false
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.updatePadding(top = bars.top)
            binding.legendScroll.updatePadding(bottom = bars.bottom)
            binding.bottomNavigation.bottomNavigation.updatePadding(bottom = bars.bottom)
            insets
        }
        binding.periodToggle.check(R.id.buttonMonth)
        binding.periodToggle.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) loadExpenses(checkedId)
        }
        loadExpenses(R.id.buttonMonth)
    }

    private fun loadExpenses(periodButtonId: Int) {
        val (startTime, label) = when (periodButtonId) {
            R.id.buttonWeek -> startOfWeek() to "Total spent this week"
            R.id.buttonYear -> startOfYear() to "Total spent this year"
            else -> startOfMonth() to "Total spent this month"
        }
        binding.tvTotalLabel.text = label
        CoroutineScope(Dispatchers.IO).launch {
            val expenses = TransactionDbBuilder().getDatabase(this@AnalyticsActivity)
                .dao.getExpenseTotalsByCategory(startTime)
            withContext(Dispatchers.Main) { displayExpenses(expenses) }
        }
    }

    private fun displayExpenses(expenses: List<CategoryExpense>) {
        val total = expenses.sumOf { it.totalAmount }
        binding.tvTotal.text = "₹${String.format(Locale.getDefault(), "%.2f", total)}"
        binding.pieChart.setExpenses(expenses)
        binding.tvEmpty.visibility = if (expenses.isEmpty()) View.VISIBLE else View.GONE
        binding.legendScroll.visibility = if (expenses.isEmpty()) View.GONE else View.VISIBLE
        binding.legendContainer.removeAllViews()
        expenses.forEachIndexed { index, expense ->
            val percent = if (total == 0.0) 0 else (expense.totalAmount / total * 100).toInt()
            val item = ItemCategoryBinding.inflate(layoutInflater, binding.legendContainer, false)
            item.tvCategory.text = expense.category
            item.tvTransactionCount.text = "${expense.transactionCount} transaction${if (expense.transactionCount == 1) "" else "s"}"
            item.tvAmount.text = "₹${String.format(Locale.getDefault(), "%.2f", expense.totalAmount)}"
            item.tvPercent.text = "$percent%"
            item.progressBar.progress = percent
            item.progressBar.progressTintList = android.content.res.ColorStateList.valueOf(binding.pieChart.colorFor(index))
            binding.legendContainer.addView(item.root)
        }
    }

    private fun startOfWeek(): Long = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
        add(Calendar.DAY_OF_YEAR, -((get(Calendar.DAY_OF_WEEK) + 5) % 7))
    }.timeInMillis

    private fun startOfMonth(): Long = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1); set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis

    private fun startOfYear(): Long = Calendar.getInstance().apply {
        set(Calendar.MONTH, Calendar.JANUARY); set(Calendar.DAY_OF_MONTH, 1)
        set(Calendar.HOUR_OF_DAY, 0); set(Calendar.MINUTE, 0); set(Calendar.SECOND, 0); set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
