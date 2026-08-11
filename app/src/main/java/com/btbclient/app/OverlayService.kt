package com.btbclient.app

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Toast
import androidx.core.app.NotificationCompat

class OverlayService : Service() {

    private lateinit var wm: WindowManager
    private var bubble: View? = null
    private var dashboard: View? = null

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        startForegroundWithNotification()
        showBubble()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        bubble?.let { runCatching { wm.removeView(it) } }
        dashboard?.let { runCatching { wm.removeView(it) } }
        bubble=null
        dashboard=null
        super.onDestroy()
    }

    private fun overlayType(): Int =
        if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        else @Suppress("DEPRECATION") WindowManager.LayoutParams.TYPE_PHONE

    private fun startForegroundWithNotification() {
        val id="btb_overlay"
        if(Build.VERSION.SDK_INT>=26){
            val c=NotificationChannel(id,"BTB Client",NotificationManager.IMPORTANCE_MIN)
            getSystemService(NotificationManager::class.java).createNotificationChannel(c)
        }
        val n=NotificationCompat.Builder(this,id)
            .setSmallIcon(android.R.drawable.ic_menu_compass)
            .setContentTitle("BTB Client")
            .setContentText("Bedrock utility overlay")
            .setOngoing(true)
            .build()
        startForeground(1,n)
    }

    private fun showBubble() {
        val v = LayoutInflater.from(this).inflate(R.layout.overlay_bubble, null)
        val p = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        p.gravity=Gravity.TOP or Gravity.START
        p.x=12; p.y=280

        var sx=0; var sy=0; var dx=0f; var dy=0f; var moved=false
        v.setOnTouchListener { _, e ->
            when(e.actionMasked){
                MotionEvent.ACTION_DOWN -> {
                    sx=p.x; sy=p.y; dx=e.rawX; dy=e.rawY; moved=false; true
                }
                MotionEvent.ACTION_MOVE -> {
                    val mx = (e.rawX - dx).toInt()
                    val my = (e.rawY - dy).toInt()
                    if (kotlin.math.abs(mx) > 6 || kotlin.math.abs(my) > 6) moved = true
                    p.x = sx + mx
                    p.y = sy + my
                    runCatching { wm.updateViewLayout(v, p) }; true
                }
                MotionEvent.ACTION_UP -> {
                    if(!moved) openDashboard()
                    true
                }
                else -> false
            }
        }
        bubble=v
        wm.addView(v,p)
    }

    private fun openDashboard() {
        if(dashboard!=null) return

        val view=BtbDashboardView(this){ closeDashboard() }
        val dm=resources.displayMetrics
        val width=(dm.widthPixels*0.96f).toInt()
        val height=(dm.heightPixels*0.90f).toInt()

        val p=WindowManager.LayoutParams(
            width,height,overlayType(),
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        )
        p.gravity=Gravity.CENTER
        dashboard=view
        wm.addView(view,p)
    }

    private fun closeDashboard() {
        dashboard?.let { runCatching { wm.removeView(it) } }
        dashboard=null
    }
}
