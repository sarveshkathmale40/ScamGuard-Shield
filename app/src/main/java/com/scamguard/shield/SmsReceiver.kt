package com.scamguard.shield

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            for (sms in messages) {
                val body = sms.messageBody ?: ""
                val result = ScamDetectionEngine.inspectText(body)

                if (result.isScam && context != null) {
                    val alertIntent = Intent(context, AlertOverlayActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        putExtra("EXTRA_REASON", result.reason)
                        putExtra("EXTRA_LINK", result.foundLink)
                    }
                    context.startActivity(alertIntent)
                }
            }
        }
    }
}
