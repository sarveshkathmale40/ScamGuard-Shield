package com.scamguard.shield

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlertOverlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert_overlay)

        val sender = intent.getStringExtra("EXTRA_SENDER") ?: "SUSPICIOUS THREAT"
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: "Malicious phishing link detected."

        findViewById<TextView>(R.id.tvSender)?.text = "Source: $sender"
        findViewById<TextView>(R.id.tvMessage)?.text = message

        findViewById<Button>(R.id.btnDismiss)?.setOnClickListener {
            finish()
        }
    }
}
