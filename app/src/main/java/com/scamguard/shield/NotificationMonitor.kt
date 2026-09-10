package com.scamguard.shield

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationMonitor : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)

        val pkg = sbn?.packageName ?: return
        val extras = sbn.notification?.extras ?: return

        // WhatsApp, Gmail, Telegram व Messaging Apps
        if (pkg.contains("whatsapp") || pkg.contains("gm") || pkg.contains("messaging") || pkg.contains("telegram")) {
            val title = extras.getString("android.title") ?: ""
            val content = extras.getCharSequence("android.text")?.toString() ?: ""
            val fullText = "$title $content"

            val result = ScamDetectionEngine.inspectText(fullText)

            if (result.isScam) {
                val intent = Intent(applicationContext, AlertOverlayActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    putExtra("EXTRA_REASON", result.reason)
                    putExtra("EXTRA_LINK", result.foundLink)
                }
                startActivity(intent)
            }
        }
    }
}
