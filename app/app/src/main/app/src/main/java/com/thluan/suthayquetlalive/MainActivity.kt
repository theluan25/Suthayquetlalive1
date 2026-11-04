package com.thluan.suthayquetlalive

import android.app.Activity
import android.os.Bundle
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Color
import android.graphics.RectF
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.content.Context
import kotlin.math.min

class MainActivity : Activity() {

    private lateinit var scene: TempleScene
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val page = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(14, 12, 14, 12)
            setBackgroundColor(Color.rgb(244, 240, 226))
        }

        page.addView(TextView(this).apply {
            text = "🪷 SƯ THẦY QUÉT LÁ LIVE"
            textSize = 21f
            setTextColor(Color.rgb(75, 55, 35))
            gravity = Gravity.CENTER
            setPadding(4, 8, 4, 10)
        })

        status = TextView(this).apply {
            text = "Trạng thái: Đang ở sân chùa"
            textSize = 14f
            gravity = Gravity.CENTER
            setTextColor(Color.DKGRAY)
            setPadding(4, 4, 4, 8)
        }
        page.addView(status)

        scene = TempleScene(this)
        page.addView(scene, LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 0, 1f
        ))

        addButtonRow(page, listOf("Quét lá", "Chạy", "Nghỉ")) {
            scene.action = it
            status.text = "Trạng thái: $it"
            scene.invalidate()
        }

        addButtonRow(page, listOf("Mưa", "Hoa", "Gió")) {
            scene.weather = it
            status.text = "Hiệu ứng: $it"
            scene.invalidate()
        }

        addButtonRow(page, listOf("Rose", "Tim", "Lion")) {
            scene.gift = it
            status.text = "Quà demo: $it"
            scene.invalidate()
        }

        val commentRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
        }

        val comment = EditText(this).apply {
            hint = "Nhập bình luận demo..."
            textSize = 14f
            singleLine = true
        }

        commentRow.addView(comment,
            LinearLayout.LayoutParams(0, 48, 1f))

        commentRow.addView(Button(this).apply {
            text = "Gửi"
            setOnClickListener {
                val message = comment.text.toString().trim()
                if (message.isNotEmpty()) {
                    status.text = "Bình luận: $message"
                    comment.setText("")
                    Toast.makeText(
                        this@MainActivity,
                        "Đã gửi bình luận demo",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

        page.addView(commentRow)

        page.addView(TextView(this).apply {
            text = "BẢN 1.0 • Mô phỏng 2D • Chưa kết nối TikTok LIVE thật"
            textSize = 11f
            gravity = Gravity.CENTER
            setTextColor(Color.GRAY)
        })

        setContentView(page)
    }

    private fun addButtonRow(
        parent: LinearLayout,
        labels: List<String>,
        onTap: (String) -> Unit
    ) {
        val row = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER
        }

        labels.forEach { label ->
            row.addView(Button(this).apply {
                text = label
                textSize = 12f
                isAllCaps = false
                setOnClickListener { onTap(label) }
            }, LinearLayout.LayoutParams(0, 48, 1f).apply {
                setMargins(3, 2, 3, 2)
            })
        }

        parent.addView(row)
    }
}

private class TempleScene(context: Context) : View(context) {

    var action = "Quét lá"
    var weather = ""
    var gift = ""

    private val p = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val w = width.toFloat()
        val h = height.toFloat()
        val scale = min(w / 360f, h / 500f)

        canvas.save()
        canvas.scale(scale, scale)

        val cw = w / scale
        val ch = h / scale

        // Bầu trời
        p.color = Color.rgb(190, 218, 218)
        canvas.drawRect(0f, 0f, cw, ch, p)

        // Đồi cây
        p.color = Color.rgb(116, 157, 103)
        canvas.drawOval(-50f, ch * .52f, cw * .45f, ch * .8f, p)
        canvas.drawOval(cw * .5f, ch * .5f, cw + 60f, ch * .82f, p)

        // Ngôi chùa
        p.color = Color.rgb(126, 75, 48)
        canvas.drawRect(cw * .28f, ch * .27f, cw * .72f, ch * .53f, p)

        p.color = Color.rgb(75, 49, 39)
        val roof = android.graphics.Path().apply {
            moveTo(cw * .22f, ch * .29f)
            lineTo(cw * .5f, ch * .16f)
            lineTo(cw * .78f, ch * .29f)
            close()
        }
        canvas.drawPath(roof, p)

        p.color = Color.rgb(238, 205, 135)
        canvas.drawRect(cw * .45f, ch * .38f, cw * .55f, ch * .53f, p)

        // Sân chùa
        p.color = Color.rgb(205, 190, 157)
        canvas.drawRect(0f, ch * .67f, cw, ch, p)

        // Cây
        p.color = Color.rgb(91, 65, 43)
        canvas.drawRect(cw * .1f, ch * .38f, cw * .14f, ch * .72f, p)

        p.color = Color.rgb(54, 112, 68)
        canvas.drawCircle(cw * .12f, ch * .35f, cw * .12f, p)
        canvas.drawCircle(cw * .06f, ch * .4f, cw * .08f, p)

        // Nhà sư
        p.color = Color.rgb(224, 190, 142)
        canvas.drawCircle(cw * .5f, ch * .57f, cw * .045f, p)

        p.color = Color.rgb(119, 73, 48)
        canvas.drawRoundRect(
            RectF(cw * .44f, ch * .61f, cw * .56f, ch * .82f),
            18f, 18f, p
        )

        // Tay và chổi
        p.color = Color.rgb(65, 49, 37)
        p.strokeWidth = 5f
        canvas.drawLine(cw * .46f, ch * .68f, cw * .36f, ch * .78f, p)
        canvas.drawLine(cw * .54f, ch * .68f, cw * .64f, ch * .76f, p)

        p.color = Color.rgb(80, 54, 35)
        canvas.drawLine(cw * .35f, ch * .78f, cw * .28f, ch * .88f, p)
        canvas.drawLine(cw * .28f, ch * .88f, cw * .67f, ch * .84f, p)
        canvas.drawLine(cw * .64f, ch * .76f, cw * .78f, ch * .9f, p)

        // Lá trên sân
        p.color = Color.rgb(191, 133, 54)
        for (i in 0..11) {
            val x = cw * (0.08f + i * 0.075f)
            val y = ch * (0.72f + (i % 3) * 0.065f)
            canvas.drawOval(x, y, x + 10f, y + 4f, p)
        }

        // Hiệu ứng mưa
        if (weather == "Mưa") {
            p.color = Color.rgb(70, 120, 160)
            p.strokeWidth = 2f
            for (i in 0..18) {
                val x = (i * 37 % 350).toFloat()
                val y = (i * 53 % 300).toFloat()
                canvas.drawLine(x, y, x - 5f, y + 13f, p)
            }
        }

        // Hiệu ứng hoa
        if (weather == "Hoa") {
            p.color = Color.MAGENTA
            for (i in 0..9) {
                canvas.drawCircle(
                    (i * 37 % 340).toFloat(),
                    (i * 29 % 250).toFloat(),
                    4f, p
                )
            }
        }

        // Hiệu ứng gió
        if (weather == "Gió") {
            p.color = Color.WHITE
            p.strokeWidth = 2f
            for (i in 0..4) {
                canvas.drawLine(
                    20f, 80f + i * 25f,
                    100f, 80f + i * 25f, p
                )
            }
        }

        p.color = Color.rgb(65, 55, 42)
        p.textSize = 15f
        canvas.drawText(action, 14f, 25f, p)

        if (gift.isNotEmpty()) {
            p.textSize = 20f
            canvas.drawText(gift, 14f, 52f, p)
        }

        canvas.restore()
    }
}