package com.scamguard.shield

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPermissions = findViewById<Button>(R.id.btnPermissions)
        val tvNews = findViewById<TextView>(R.id.tvDailyScamNews)

        tvNews.text = """
            📢 आजचे सायबर अलर्ट्स (Live News):
            
            1. ⚡ महावितरण वीज बिल मेसेज फ्रॉड: 'रात्री वीज कट होईल' असा मेसेज आल्यास दिलेल्या नंबरवर फोन करू नका.
            2. 💳 बँक APK स्कॅम: व्हॉट्सॲपवर येणाऱ्या 'SBI_Reward.apk' फाईल्स इन्स्टॉल करू नका.
            3. 🎟️ PM किसान योजना फ्री गिफ्ट लिंक्स पूर्णपणे खोट्या आहेत.
        """.trimIndent()

        btnPermissions.setOnClickListener {
            val notifIntent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
            startActivity(notifIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
                val overlayIntent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(overlayIntent)
            }

            Toast.makeText(this, "कृपया सर्व परवानग्या सुरू करा!", Toast.LENGTH_LONG).show()
        }
    }
}
