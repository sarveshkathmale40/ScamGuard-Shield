package com.scamguard.shield

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val PERMISSION_REQUEST_CODE = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ॲप चालू होताच आपोआप परमिशन स्क्रीन उघडेल
        requestAllSystemPermissions()

        val btnPermissions = findViewById<Button>(R.id.btnPermissions)
        btnPermissions?.setOnClickListener {
            requestAllSystemPermissions()
        }

        // Live Demo Button
        val btnSimulate = findViewById<Button?>(R.id.btnSimulateScam)
        btnSimulate?.setOnClickListener {
            val intent = Intent(this, AlertOverlayActivity::class.java).apply {
                putExtra("EXTRA_SENDER", "VM-MAHADISCOM-ALERT")
                putExtra("EXTRA_MESSAGE", "CRITICAL WARNING: Power will be disconnected tonight. Download APK: http://mahadiscom-fraud.apk")
            }
            startActivity(intent)
        }
    }

    private fun requestAllSystemPermissions() {
        val permissions = mutableListOf<String>()

        // 1. SMS Permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.RECEIVE_SMS)
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_SMS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        if (permissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissions.toTypedArray(), PERMISSION_REQUEST_CODE)
        }

        // 2. Display Over Other Apps Permission (थेट सिस्टीम स्क्रीन उघडेल)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
                Toast.makeText(this, "Please enable 'Allow display over other apps'", Toast.LENGTH_LONG).show()
            }
        }
    }
}
