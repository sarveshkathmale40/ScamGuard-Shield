package com.scamguard.shield

import android.app.KeyguardManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AlertOverlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val km = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
            km.requestDismissKeyguard(this, null)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
            )
        }

        setContentView(R.layout.activity_alert_overlay)

        val sender = intent.getStringExtra("EXTRA_SENDER") ?: "SUSPICIOUS SENDER"
        val message = intent.getStringExtra("EXTRA_MESSAGE") ?: ""

        findViewById<TextView>(R.id.tvSender)?.text = "Sender: $sender"
        findViewById<TextView>(R.id.tvMessage)?.text = "THIS LINK IS MALICIOUS!\n\nDO NOT CLICK IT!\n\nDetected Message:\n$message"

        findViewById<Button>(R.id.btnDismiss)?.setOnClickListener {
            finish()
        }
    }
}
