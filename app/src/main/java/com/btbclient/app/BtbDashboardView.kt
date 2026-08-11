package com.btbclient.app

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.max
import kotlin.math.min

class BtbDashboardView(
    context: Context,
    private val onClose: () -> Unit
) : View(context) {

    private val bg = Paint(Paint.ANTI_ALIAS_FLAG)
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG)
    private val text = Paint(Paint.ANTI_ALIAS_FLAG)
    private val sub = Paint(Paint.ANTI_ALIAS_FLAG)

    private val purple = Color.rgb(167, 106, 255)
    private val blue = Color.rgb(65, 140, 255)
    private val green = Color.rgb(55, 221, 126)
    private val cyan = Color.rgb(65, 210, 220)
    private val amber = Color.rgb(245, 192, 55)
    private val pink = Color.rgb(236, 83, 164)

    private var downX = 0f
    private var downY = 0f

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        text.typeface = Typeface.create("sans", Typeface.NORMAL)
        sub.typeface = Typeface.create("sans", Typeface.NORMAL)
        stroke.style = Paint.Style.STROKE
        stroke.strokeWidth = dp(1f)
    }

    private fun dp(v: Float) = v * resources.displayMetrics.density

    private fun roundRect(c: Canvas, l: Float, t: Float, r: Float, b: Float, radius: Float, color: Int) {
        bg.style = Paint.Style.FILL
        bg.color = color
        c.drawRoundRect(l, t, r, b, radius, radius, bg)
    }

    private fun outline(c: Canvas, l: Float, t: Float, r: Float, b: Float, radius: Float, color: Int) {
        stroke.color = color
        stroke.style = Paint.Style.STROKE
        stroke.strokeWidth = dp(1.2f)
        c.drawRoundRect(l, t, r, b, radius, radius, stroke)
    }

    private fun txt(c: Canvas, s: String, x: Float, y: Float, size: Float, color: Int, bold: Boolean=false) {
        text.textSize = dp(size)
        text.color = color
        text.typeface = Typeface.create("sans", if (bold) Typeface.BOLD else Typeface.NORMAL)
        c.drawText(s, x, y, text)
    }

    private fun centerTxt(c: Canvas, s: String, cx: Float, y: Float, size: Float, color: Int, bold: Boolean=false) {
        text.textSize = dp(size)
        text.typeface = Typeface.create("sans", if (bold) Typeface.BOLD else Typeface.NORMAL)
        txt(c, s, cx - text.measureText(s)/2f, y, size, color, bold)
    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        val w = width.toFloat()
        val h = height.toFloat()
        val d = resources.displayMetrics.density

        // Background
        bg.shader = LinearGradient(0f, 0f, w, h,
            Color.rgb(17, 13, 25), Color.rgb(7, 12, 19), Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w, h, bg)
        bg.shader = null

        val pad = dp(12f)
        val headerH = dp(72f)
        val sidebarW = dp(190f)
        val rightW = dp(235f)
        val gap = dp(10f)

        // Header
        roundRect(c, pad, pad, w-pad, pad+headerH, dp(14f), Color.argb(215, 19, 17, 25))
        txt(c, "◆", pad+dp(16f), pad+dp(42f), 26f, purple, true)
        txt(c, "BTB CLIENT", pad+dp(52f), pad+dp(38f), 27f, Color.WHITE, true)
        txt(c, "Minecraft Bedrock Edition", pad+dp(53f), pad+dp(58f), 12f, Color.LTGRAY)
        txt(c, "●", w-pad-dp(220f), pad+dp(33f), 13f, green, true)
        txt(c, "Online", w-pad-dp(201f), pad+dp(35f), 12f, Color.WHITE)
        txt(c, "v1.0.0", w-pad-dp(198f), pad+dp(55f), 10f, Color.GRAY)
        roundRect(c, w-pad-dp(88f), pad+dp(12f), w-pad-dp(48f), pad+dp(52f), dp(9f), Color.argb(90,255,255,255))
        centerTxt(c, "—", w-pad-dp(68f), pad+dp(35f), 18f, Color.WHITE)
        roundRect(c, w-pad-dp(43f), pad+dp(12f), w-pad-dp(5f), pad+dp(52f), dp(9f), Color.argb(90,255,255,255))
        centerTxt(c, "×", w-pad-dp(24f), pad+dp(38f), 22f, Color.WHITE)

        val top = pad+headerH+gap
        val bottom = h-pad-dp(58f)

        // Sidebar
        roundRect(c, pad, top, pad+sidebarW, bottom, dp(12f), Color.argb(175, 16, 18, 26))
        val nav = arrayOf("⌂  Home","◯  Profile","⚙  Settings","◇  Appearance","◎  Language","⌁  Key Binds","□  Resource Packs","☷  Friends","ⓘ  About")
        var ny = top+dp(55f)
        nav.forEachIndexed { i, item ->
            if (i==0) roundRect(c, pad+dp(12f), ny-dp(28f), pad+sidebarW-dp(12f), ny+dp(18f), dp(8f), Color.argb(150,82,45,145))
            txt(c, item, pad+dp(24f), ny, 13f, if(i==0) Color.WHITE else Color.LTGRAY, i==0)
            ny += dp(47f)
        }

        // Main grid
        val mainL = pad+sidebarW+gap
        val mainR = w-pad-rightW-gap
        val mainW = mainR-mainL
        roundRect(c, mainL, top, mainR, bottom, dp(12f), Color.argb(150, 11, 15, 22))
        txt(c, "│ MAIN MENU", mainL+dp(16f), top+dp(33f), 14f, purple, true)

        val cols=4
        val rows=3
        val inner=dp(14f)
        val cardGap=dp(9f)
        val cardW=(mainW-inner*2-cardGap*(cols-1))/cols
        val cardH=dp(151f)
        val cards=listOf(
            Triple("GUI","Customize your\nin-game interface",purple),
            Triple("MAP","View world map\nand landmarks",green),
            Triple("ASD BUTTON","Edit movement\nbutton layout",blue),
            Triple("F5","Switch perspective\n(First/Third person)",amber),
            Triple("CONTROLS","Manage touch\n& control settings",cyan),
            Triple("INVENTORY VIEWER","Quick view your\ninventory & chests",Color.rgb(245,125,55)),
            Triple("TASKS","Track your in-game\ntasks & goals",pink),
            Triple("FRIENDS","Manage friends\nand invites",purple),
            Triple("SCREENSHOT","Capture and view\nyour screenshots",blue),
            Triple("WORLD MANAGER","Manage your worlds\nand backups",amber),
            Triple("SETTINGS","General client\nsettings",cyan),
            Triple("ABOUT","About BTB Client\nand updates",Color.rgb(170,160,220))
        )

        var idx=0
        for(r in 0 until rows){
            for(col in 0 until cols){
                val l=mainL+inner+col*(cardW+cardGap)
                val t=top+dp(47f)+r*(cardH+cardGap)
                val rr=l+cardW
                val bb=t+cardH
                val item=cards[idx++]
                roundRect(c,l,t,rr,bb,dp(13f),Color.argb(165,23,24,31))
                outline(c,l,t,rr,bb,dp(13f),item.third)
                centerTxt(c,item.first, (l+rr)/2f, t+dp(83f), if(item.first.length>13) 11f else 13f, item.third, true)
                val lines=item.second.split("\n")
                lines.forEachIndexed { li, line ->
                    centerTxt(c,line,(l+rr)/2f,t+dp(106f+li*15f),9.5f,Color.LTGRAY)
                }
                // simple icon placeholder
                centerTxt(c, when(item.first) {
                    "GUI"->"▣"; "MAP"->"◈"; "ASD BUTTON"->"W A S D"; "F5"->"□"
                    "CONTROLS"->"≡"; "INVENTORY VIEWER"->"▢"; "TASKS"->"✓"; "FRIENDS"->"●●"
                    "SCREENSHOT"->"▧"; "WORLD MANAGER"->"▰"; "SETTINGS"->"⚙"; else->"ⓘ"
                }, (l+rr)/2f, t+dp(48f), 17f, item.third, true)
            }
        }

        // Right column
        val rL=mainR+gap
        val rR=w-pad
        val infoH=dp(182f)
        roundRect(c,rL,top,rR,top+infoH,dp(12f),Color.argb(165,18,20,27))
        txt(c,"│ PLAYER INFO",rL+dp(14f),top+dp(34f),14f,purple,true)
        roundRect(c,rL+dp(15f),top+dp(54f),rL+dp(70f),top+dp(109f),dp(7f),Color.rgb(92,70,53))
        centerTxt(c,"P",rL+dp(42f),top+dp(91f),22f,Color.WHITE,true)
        txt(c,"Player",rL+dp(84f),top+dp(83f),14f,Color.WHITE,true)
        txt(c,"Bedrock data: waiting",rL+dp(84f),top+dp(104f),10f,Color.LTGRAY)
        txt(c,"XYZ: —  —  —",rL+dp(15f),top+dp(136f),11f,Color.LTGRAY)
        txt(c,"Dimension: —",rL+dp(15f),top+dp(155f),11f,Color.LTGRAY)
        txt(c,"World: —",rL+dp(15f),top+dp(174f),11f,Color.LTGRAY)

        val quickTop=top+infoH+gap
        roundRect(c,rL,quickTop,rR,quickTop+dp(280f),dp(12f),Color.argb(165,18,20,27))
        txt(c,"│ QUICK ACCESS",rL+dp(14f),quickTop+dp(33f),14f,purple,true)
        val quick=arrayOf("⌂  Home","↻  Respawn","⚙  Settings","▣  Marketplace","⇥  Save & Quit")
        var qy=quickTop+dp(61f)
        quick.forEach {
            roundRect(c,rL+dp(12f),qy,rR-dp(12f),qy+dp(42f),dp(7f),Color.argb(125,45,43,43))
            txt(c,it,rL+dp(25f),qy+dp(27f),12f,Color.WHITE)
            qy+=dp(48f)
        }

        val newsTop=quickTop+dp(290f)
        roundRect(c,rL,newsTop,rR,bottom,dp(12f),Color.argb(165,18,20,27))
        txt(c,"●  NEWS",rL+dp(14f),newsTop+dp(31f),13f,purple,true)
        txt(c,"BTB Client v1.0.0",rL+dp(14f),newsTop+dp(63f),11f,Color.LTGRAY)
        txt(c,"Official release",rL+dp(14f),newsTop+dp(84f),10f,Color.GRAY)

        // Bottom status
        val by=h-pad-dp(47f)
        roundRect(c,pad,by,w-pad,h-pad,dp(11f),Color.argb(205,13,17,23))
        txt(c,"◆",pad+dp(18f),by+dp(28f),18f,purple,true)
        txt(c,"BTB Client",pad+dp(43f),by+dp(22f),12f,purple,true)
        txt(c,"Built for Minecraft Bedrock",pad+dp(43f),by+dp(38f),9f,Color.GRAY)
        txt(c,"▥  Performance",pad+sidebarW+dp(22f),by+dp(22f),10f,Color.LTGRAY)
        txt(c,"Good",pad+sidebarW+dp(22f),by+dp(39f),10f,green,true)
        txt(c,"▯  Device",pad+sidebarW+dp(185f),by+dp(22f),10f,Color.LTGRAY)
        txt(c,"Android",pad+sidebarW+dp(185f),by+dp(39f),10f,Color.LTGRAY)
        txt(c,"⌁  Connection",pad+sidebarW+dp(310f),by+dp(22f),10f,Color.LTGRAY)
        txt(c,"Bridge waiting",pad+sidebarW+dp(310f),by+dp(39f),10f,amber,true)
        txt(c,"BTB",w-pad-dp(48f),by+dp(30f),11f,purple,true)
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when(e.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX=e.x; downY=e.y
                return true
            }
            MotionEvent.ACTION_UP -> {
                val dx=e.x-downX
                val dy=e.y-downY
                if (kotlin.math.abs(dx)<20 && kotlin.math.abs(dy)<20) {
                    val d=resources.displayMetrics.density
                    val x=e.x/d; val y=e.y/d
                    // Header close button
                    if (x > width/d-75 && y < 75) {
                        onClose()
                        return true
                    }
                    Toast.makeText(context,"BTB Client • utility menu",Toast.LENGTH_SHORT).show()
                }
                return true
            }
        }
        return true
    }
}
