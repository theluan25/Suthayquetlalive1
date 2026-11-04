
package com.thluan.suthayquetlalive

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.view.View
import android.widget.*
import kotlin.random.Random

class MainActivity : Activity() {

    private lateinit var scene: TempleScene
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(12, 12, 12, 12)
            setBackgroundColor(Color.rgb(30, 40, 32))
        }

        val title = TextView(this).apply {
            text = "🍂 SƯ THẦY QUÉT LÁ LIVE"
            textSize = 21f
            gravity = 17
            setTextColor(Color.WHITE)
        }
        root.addView(title)

        scene = TempleScene(this)
        root.addView(scene, LinearLayout.LayoutParams(-1, 0, 1f))

        status = TextView(this).apply {
            text = "🙏 Nam mô A Di Đà Phật"
            textSize = 16f
            gravity = 17
            setTextColor(Color.WHITE)
        }
        root.addView(status)

        val commands = listOf(
            listOf("Quét lá", "Chạy", "Nghỉ"),
            listOf("Mưa", "Hoa", "Gió"),
            listOf("Rose", "Tim", "Lion")
        )

        commands.forEach { items ->
            val row = LinearLayout(this)
            items.forEach { item ->
                val button = Button(this).apply {
                    text = item
                    isAllCaps = false
                    setOnClickListener { action(item) }
                }
                row.addView(button, LinearLayout.LayoutParams(0, 55, 1f))
            }
            root.addView(row)
        }

        val input = EditText(this).apply {
            hint = "Nhập bình luận thử..."
            setSingleLine(true)
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }
        root.addView(input)

        val send = Button(this).apply {
            text = "Gửi bình luận thử"
            setOnClickListener {
                action(input.text.toString())
                input.text.clear()
            }
        }
        root.addView(send)

        setContentView(root)
    }

    private fun action(value: String) {
        val text = value.lowercase()

        status.text = when {
            "quét" in text -> "🍂 Sư thầy đang quét lá!"
            "chạy" in text -> "🏃 Sư thầy đang chạy!"
            "nghỉ" in text -> "🙏 Sư thầy đang nghỉ."
            "mưa" in text -> "🌧 Trời đang mưa!"
            "hoa" in text || "rose" in text -> "🌸 Cảm ơn món quà hoa!"
            "tim" in text || "heart" in text -> "❤️ Sư thầy chắp tay cảm ơn!"
            "lion" in text -> "🦁 Cảm ơn món quà đặc biệt!"
            "gió" in text -> "🍃 Gió thổi lá bay!"
            else -> "🙏 Cảm ơn bạn đã tương tác!"
        }

        scene.setAction(text)
    }
}

class TempleScene(context: android.content.Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var action = "quét"
    private val leaves = mutableListOf<Pair<Float, Float>>()

    init {
        repeat(35) {
            leaves.add(Random.nextFloat() to Random.nextFloat())
        }
    }

    fun setAction(value: String) {
        action = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()

        paint.shader = LinearGradient(
            0f, 0f, 0f, h,
            Color.rgb(255, 190, 115),
            Color.rgb(62, 81, 62),
            Shader.TileMode.CLAMP
        )
        canvas.drawRect(0f, 0f, w, h, paint)
        paint.shader = null

        paint.color = Color.rgb(82, 105, 78)
        val mountain = Path().apply {
            moveTo(0f, h * .45f)
            lineTo(w * .25f, h * .20f)
            lineTo(w * .52f, h * .45f)
            lineTo(w * .78f, h * .22f)
            lineTo(w, h * .42f)
            lineTo(w, h)
            lineTo(0f, h)
            close()
        }
        canvas.drawPath(mountain, paint)

        paint.color = Color.rgb(75, 39, 27)
        val roof = Path().apply {
            moveTo(w * .16f, h * .38f)
            lineTo(w * .5f, h * .20f)
            lineTo(w * .84f, h * .38f)
            close()
        }
        canvas.drawPath(roof, paint)

        paint.color = Color.rgb(205, 160, 95)
        canvas.drawRect(w*.25f, h*.37f, w*.75f, h*.58f, paint)

        paint.color = Color.rgb(155, 126, 91)
        canvas.drawRect(0f, h*.58f, w, h, paint)

        paint.color = Color.rgb(239, 166, 46)
        leaves.forEachIndexed { index, leaf ->
            val x = leaf.first * w
            val y = leaf.second * h
            canvas.drawOval(x, y, x+9f, y+6f, paint)
            leaves[index] = leaf.first to
                ((leaf.second + 0.008f) % 1f)
        }

        val x = w * .5f
        val y = h * .67f

        paint.color = Color.rgb(133, 78, 46)
        canvas.drawRoundRect(x-44f, y-12f, x+44f, y+105f, 28f, 28f, paint)

        paint.color = Color.rgb(221, 174, 132)
        canvas.drawCircle(x, y-45f, 25f, paint)

        paint.color = Color.rgb(213, 165, 83)
        paint.strokeWidth = 7f
        canvas.drawLine(x+38f, y+8f, x+85f, y+105f, paint)
        paint.strokeWidth = 1f

        paint.color = Color.rgb(230, 192, 118)
        canvas.drawLine(x+64f, y+100f, x+106f, y+113f, paint)

        postInvalidateDelayed(60)
    }
}
