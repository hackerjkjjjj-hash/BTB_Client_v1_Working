package com.btbclient.app

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var statusText: TextView
    private lateinit var btnGrant: Button
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        btnGrant = findViewById(R.id.btnGrantPermission)
        btnStart = findViewById(R.id.btnStartOverlay)
        btnStop = findViewById(R.id.btnStopOverlay)

        btnGrant.setOnClickListener {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        btnStart.setOnClickListener {
            startService(Intent(this, OverlayService::class.java))
        }

        btnStop.setOnClickListener {
            stopService(Intent(this, OverlayService::class.java))
        }

        refreshStatus()
    }

    override fun onResume() {
        super.onResume()
        refreshStatus()
    }

    private fun refreshStatus() {
        val hasPermission = Settings.canDrawOverlays(this)
        if (hasPermission) {
            statusText.text = "Permission ត្រូវបានអនុញ្ញាតរួចហើយ ✓"
            btnGrant.isEnabled = false
            btnStart.isEnabled = true
        } else {
            statusText.text = getString(R.string.permission_needed)
            btnGrant.isEnabled = true
            btnStart.isEnabled = false
        }
    }
}
