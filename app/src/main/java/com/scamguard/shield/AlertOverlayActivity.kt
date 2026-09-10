package com.scamguard.shield

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle

class AlertOverlayActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val reason = intent.getStringExtra("EXTRA_REASON") ?: "धोकादायक लिंक आढळली!"
        val link = intent.getStringExtra("EXTRA_LINK") ?: ""

        AlertDialog.Builder(this)
            .setTitle("🚨 सायबर फ्रॉड अलर्ट!")
            .setMessage("सावधान! आलेल्या मेसेजमध्ये फसवणुकीचा धोका आहे.\n\nकारण: $reason\nधोकादायक लिंक: $link\n\nया लिंकवर क्लिक करू नका आणि कोणताही OTP देऊ नका!")
            .setCancelable(false)
            .setPositiveButton("समजले (Dismiss)") { dialog, _ ->
                dialog.dismiss()
                finish()
            }
            .show()
    }
}
