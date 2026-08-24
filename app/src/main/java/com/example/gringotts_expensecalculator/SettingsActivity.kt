package com.example.gringotts_expensecalculator

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.example.gringotts_expensecalculator.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.bottomNavigation.bottomNavigation.selectedItemId = R.id.navigation_settings
        binding.bottomNavigation.bottomNavigation.setOnItemSelectedListener {
            val target = when (it.itemId) {
                R.id.navigation_transactions -> TransactionsActivity::class.java
                R.id.navigation_categories -> AnalyticsActivity::class.java
                else -> return@setOnItemSelectedListener false
            }
            startActivity(Intent(this, target)); finish(); true
        }
        binding.enableNotifications.setOnClickListener {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS))
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            binding.root.updatePadding(top = bars.top)
            binding.bottomNavigation.bottomNavigation.updatePadding(bottom = bars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        val component = ComponentName(this, PaymentNotificationListener::class.java)
        val enabled = getSystemService(android.app.NotificationManager::class.java).isNotificationListenerAccessGranted(component)
        binding.notificationStatus.text = if (enabled) "Payment notification access is enabled" else "Payment notification access is not enabled"
    }
}
