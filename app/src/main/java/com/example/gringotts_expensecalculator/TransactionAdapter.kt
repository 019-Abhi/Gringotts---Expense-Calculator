package com.example.gringotts_expensecalculator

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gringotts_expensecalculator.databinding.ItemTransactionBinding
import androidx.core.graphics.toColorInt
import java.text.DateFormat
import java.util.Date

class TransactionAdapter(private val items: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = items[position]
        with(holder.binding) {
            tvMerchant.text = t.merchant ?: "Unknown Merchant"
            tvSender.text = t.sender
            tvAmount.text = "₹${"%.2f".format(t.amount)}"
            tvType.text = t.type
            tvCategory.text = t.category
            tvDate.text = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(Date(t.timestamp))

            val color = if (t.type == "debit") "#E53935".toColorInt() else "#43A047".toColorInt()
            tvAmount.setTextColor(color)
            viewTypeDot.backgroundTintList =
                android.content.res.ColorStateList.valueOf(color)
        }
    }

    override fun getItemCount() = items.size
}
