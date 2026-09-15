package com.scamguard.shield

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import androidx.core.app.NotificationCompat

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

            // फ्रॉड लिंक्स आणि कीवर्ड्स तपासणे
            if (lowerText.contains("http://") || lowerText.contains("https://") ||
                lowerText.contains("www.") || lowerText.contains(".com") ||
                lowerText.contains(".in") || lowerText.contains(".apk") ||
                lowerText.contains("bill") || lowerText.contains("kyc") ||
                lowerText.contains("electricity") || lowerText.contains("bank") ||
                lowerText.contains("urgent") || lowerText.contains("click")) {

                val overlayIntent = Intent(context, AlertOverlayActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    putExtra("EXTRA_SENDER", sender)
                    putExtra("EXTRA_MESSAGE", fullText)
                }

                // बॅकग्राउंडमधून स्क्रीन थेट उघडण्यासाठी FullScreen PendingIntent
                val pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    overlayIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                val channelId = "scam_alert_channel"
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val channel = NotificationChannel(
                        channelId,
                        "Critical Scam Alert",
                        NotificationManager.IMPORTANCE_HIGH
                    ).apply {
                        description = "Triggers Full Screen Alert on Phishing SMS"
                        setBypassDnd(true)
                        enableVibration(true)
                    }
                    notificationManager.createNotificationChannel(channel)
                }

                val notificationBuilder = NotificationCompat.Builder(context, channelId)
                    .setSmallIcon(android.R.drawable.ic_dialog_alert)
                    .setContentTitle("CRITICAL SCAM DETECTED!")
                    .setContentText("Malicious link detected from $sender")
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setCategory(NotificationCompat.CATEGORY_ALARM)
                    .setFullScreenIntent(pendingIntent, true)
                    .setAutoCancel(true)

                notificationManager.notify(1001, notificationBuilder.build())

                // थेट उघडण्याचा प्रयत्न
                try {
                    context.startActivity(overlayIntent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
