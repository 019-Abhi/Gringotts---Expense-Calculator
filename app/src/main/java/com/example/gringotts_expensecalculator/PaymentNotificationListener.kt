package com.example.gringotts_expensecalculator

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PaymentNotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(notification: StatusBarNotification) {
        val packageName = notification.packageName
        if (packageName !in supportedPackages) return
        val extras = notification.notification.extras
        val title = extras.getCharSequence("android.title")?.toString().orEmpty()
        val text = extras.getCharSequence("android.text")?.toString().orEmpty()
        val body = "$title $text".trim()
        if (body.isBlank()) return
        CoroutineScope(Dispatchers.IO).launch {
            TransactionIngestionRepository.ingest(this@PaymentNotificationListener, packageName, body, "NOTIFICATION", notification.postTime)
        }
    }

    private companion object {
        val supportedPackages = setOf("com.google.android.apps.nbu.paisa.user", "com.phonepe.app", "net.one97.paytm")
    }
}
