package com.example.gringotts_expensecalculator

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val sender = sms.originatingAddress
                val body = sms.messageBody

                Log.d("SMS_TRACKER", "Sender: $sender")
                Log.d("SMS_TRACKER", "Message: $body")

                val transaction = TransactionParser.parse(sender?: "Unknown", body)

                if (transaction != null) {
                    val db = TransactionDbBuilder().getDatabase(context)
                    CoroutineScope(Dispatchers.IO).launch {
                        db.dao.insertTransaction(transaction)
                        Log.d("SMS_TRACKER", "Transaction added: $transaction")
                    }
                }
            }
        }
    }
}