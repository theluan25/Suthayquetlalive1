
package com.thluan.suthayquetlalive

import android.app.Activity
import android.os.Bundle
import android.graphics.*
import android.view.View
import android.widget.*
import android.graphics.drawable.GradientDrawable
import kotlin.random.Random

class MainActivity : Activity() {

    private lateinit var scene: TempleScene
    private lateinit var status: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(24, 34, 29))
            setPadding(12, 12, 12, 12)
        }

        val title = TextView(this).apply {
            text = "🍂 SƯ THẦY QUÉT LÁ LIVE"
            textSize = 21f
            setTextColor(Color.WHITE)
            gravity = 17
            setPadding(4, 12, 4, 12)
        }
        root.addView(title)

        scene = TempleScene(this)
        root.addView(
            scene,
            LinearLayout.LayoutParams(
                -1, 0, 1f
            )
        )

        status = TextView(this).apply {
            text = "🙏 Nam mô A Di Đà Phật"
            textSize = 15f
            setTextColor(Color.WHITE)
            gravity = 17
            setPadding(5, 10, 5, 10)
        }
        root.addView(status)

        val commands = listOf(
            listOf("🍂 Quét lá", "🏃 Chạy", "🙏 Nghỉ"),
            listOf("🌧 Mưa", "🌸 Hoa", "🍃 Gió"),
            listOf("🌟 Quà Rose", "❤️ Tim", "🦁 Lion")
        )

        for (line in commands) {
            val row = LinearLayout(this).apply {
                orientation = LinearLayout.HORIZONTAL
            }

            for (command in line) {
                val button = Button(this).apply {
                    text = command
                    textSize = 12f
                    isAllCaps = false
                    setOnClickListener {
                        action(command)
                    }
                }
                row.addView(
                    button,
                    LinearLayout.LayoutParams(0, 54, 1f)
                )
            }
            root.addView(row)
        }

        val comment = EditText(this).apply {
            hint = "Nhập bình luận thử: quét, chạy, nghỉ..."
            setSingleLine(true)
            textSize = 14f
            setTextColor(Color.WHITE)
            setHintTextColor(Color.LTGRAY)
        }
        root.addView(comment)

        val send = Button(this).apply {
            text = "Gửi bình luận thử"
            setOnClickListener {
                action(comment.text.toString())
                comment.text.clear()
            }
        }
        root.addView(send)

        setContentView(root)
    }

    private fun action(value: String) {
        val text = value.lowercase()
        val message = when {
            text.contains("quét") -> "🍂 Sư thầy đang quét lá!"
            text.contains("chạy") -> "🏃 Sư thầy chạy quanh sân!"
            text.contains("nghỉ") -> "🙏 Sư thầy đang nghỉ ngơi."
            text.contains("mưa") -> "🌧 Trời bắt đầu đổ mưa!"
            text.contains("hoa") ||
                text.contains("rose") -> "🌸 Cảm ơn món quà hoa!"
            text.contains("tim") ||
                text.contains("heart") -> "❤️ Sư thầy chắp tay cảm ơn!"
            text.contains("lion") ||
                text.contains("sư tử") -> "🦁 Một món quà thật tuyệt!"
            text.contains("gió") -> "🍃 Gió thổi lá bay!"
            else -> "🙏 Cảm ơn bạn đã tương tác!"
        }

        status.text = message
        scene.setAction(text)
    }
}

class TempleScene(context: android.content.Context) : View(context) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var action = "quét"
    private val leaves = mutableListOf<Leaf>()

    data class Leaf(
        var x: Float,
        var y: Float,
        var speed: Float,
        var size: Float
    )

    init {
        repeat(35) {
            leaves.add(
                Leaf(
                    Random.nextFloat(),
                    Random.nextFloat(),
                    0.002f + Random.nextFloat() * 0.004f,
                    5f + Random.nextFloat() * 10f
                )
            )
        }
    }

    fun setAction(value: String) {
        action = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

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

        // Núi phía xa
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

        // Mái chùa
        paint.color = Color.rgb(75, 39, 27)
        val roof = Path().apply {
            moveTo(w * .16f, h * .38f)
            lineTo(w * .5f, h * .20f)
            lineTo(w * .84f, h * .38f)
            lineTo(w * .75f, h * .37f)
            lineTo(w * .5f, h * .29f)
            lineTo(w * .25f, h * .37f)
            close()
        }
        canvas.drawPath(roof, paint)

        paint.color = Color.rgb(205, 160, 95)
        canvas.drawRect(
            w * .25f, h * .37f,
            w * .75f, h * .58f, paint
        )

        // Sân chùa
        paint.color = Color.rgb(155, 126, 91)
        canvas.drawRect(0f, h * .58f, w, h, paint)

        // Lá rơi
        paint.color = Color.rgb(239, 166, 46)
        for (leaf in leaves) {
            canvas.drawOval(
                leaf.x * w,
                leaf.y * h,
                leaf.x * w + leaf.size,
                leaf.y * h + leaf.size * .6f,
                paint
            )
            leaf.y += leaf.speed
            if (leaf.y > 1f) {
                leaf.y = 0f
                leaf.x = Random.nextFloat()
            }
        }

        drawMonk(canvas, w, h)
        if (action.contains("mưa")) {
            paint.color = Color.WHITE
            repeat(35) {
                val x = Random.nextFloat() * w
                val y = Random.nextFloat() * h
                canvas.drawLine(x, y, x - 5f, y + 18f, paint)
            }
        }

        if (action.contains("hoa") || action.contains("rose")) {
            paint.textSize = 42f
            paint.color = Color.WHITE
            canvas.drawText("🌸", w * .72f, h * .65f, paint)
        }

        postInvalidateDelayed(60)
    }

    private fun drawMonk(canvas: Canvas, w: Float, h: Float) {
        val x = w * .5f
        val y = h * .67f

        // Áo nâu
        paint.color = Color.rgb(133, 78, 46)
        canvas.drawRoundRect(
            x - 44f, y - 12f,
            x + 44f, y + 105f,
            28f, 28f, paint
        )

        // Đầu
        paint.color = Color.rgb(221, 174, 132)
        canvas.drawCircle(x, y - 45f, 25f, paint)

        // Y áo
        paint.color = Color.rgb(91, 53, 37)
        canvas.drawLine(
            x - 20f, y - 18f,
            x + 20f, y + 35f, paint
        )

        // Chổi tre
        paint.color = Color.rgb(213, 165, 83)
        paint.strokeWidth = 7f
        val movement = if (action.contains("quét")) 30f else 0f
        canvas.drawLine(
            x + 38f, y + 8f,
            x + 85f + movement, y + 105f, paint
        )
        paint.strokeWidth = 1f

        paint.color = Color.rgb(230, 192, 118)
        canvas.drawLine(
            x + 64f + movement, y + 100f,
            x + 106f + movement, y + 113f,
            paint
        )
    }
}
