package com.example.gringotts_expensecalculator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class PieChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var expenses: List<CategoryExpense> = emptyList()
    private val colors = intArrayOf(
        Color.rgb(103, 80, 164), Color.rgb(44, 112, 180), Color.rgb(35, 143, 110),
        Color.rgb(234, 117, 39), Color.rgb(197, 67, 106), Color.rgb(128, 95, 57),
        Color.rgb(85, 119, 137), Color.rgb(140, 99, 193)
    )

    fun setExpenses(items: List<CategoryExpense>) {
        expenses = items
        invalidate()
    }

    fun colorFor(index: Int) = colors[index % colors.size]

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (expenses.isEmpty()) return

        val size = min(width, height).toFloat()
        val inset = size * 0.06f
        val bounds = RectF((width - size) / 2f + inset, (height - size) / 2f + inset,
            (width + size) / 2f - inset, (height + size) / 2f - inset)
        val total = expenses.sumOf { it.totalAmount }.takeIf { it > 0 } ?: return
        var startAngle = -90f

        expenses.forEachIndexed { index, expense ->
            val sweep = (expense.totalAmount / total * 360.0).toFloat()
            paint.color = colorFor(index)
            canvas.drawArc(bounds, startAngle, sweep, true, paint)
            startAngle += sweep
        }

        paint.color = Color.rgb(28, 33, 41)
        val center = size * 0.25f
        canvas.drawCircle(width / 2f, height / 2f, center, paint)
        paint.color = Color.WHITE
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = size * 0.09f
        paint.typeface = android.graphics.Typeface.DEFAULT_BOLD
        canvas.drawText("Expenses", width / 2f, height / 2f + paint.textSize / 3f, paint)
    }
}
