package com.scamguard.shield

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return

        if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
            val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
            val bodyBuilder = StringBuilder()
            var sender = "Unknown Sender"

            for (msg in messages) {
                sender = msg.displayOriginatingAddress ?: "SMS Alert"
                bodyBuilder.append(msg.displayMessageBody)
            }

            val fullText = bodyBuilder.toString()
            val lowerText = fullText.lowercase()

            // साध्या SMS मध्ये लिंक किंवा संशयास्पद शब्द येताच लाल स्क्रीन उघडणे
            if (lowerText.contains("http://") || lowerText.contains("https://") ||
                lowerText.contains("www.") || lowerText.contains(".com") ||
                lowerText.contains(".in") || lowerText.contains(".apk") ||
                lowerText.contains("bill") || lowerText.contains("kyc") ||
                lowerText.contains("electricity") || lowerText.contains("bank") ||
                lowerText.contains("urgent") || lowerText.contains("click")) {

                val overlayIntent = Intent(context, AlertOverlayActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("EXTRA_SENDER", sender)
                    putExtra("EXTRA_MESSAGE", fullText)
                }
                context.startActivity(overlayIntent)
            }
        }
    }
}
