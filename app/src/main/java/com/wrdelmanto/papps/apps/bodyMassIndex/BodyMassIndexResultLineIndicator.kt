package com.wrdelmanto.papps.apps.bodyMassIndex

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Paint.Style.FILL
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import com.wrdelmanto.papps.R
import com.wrdelmanto.papps.TWENTY_FIVE_PERCENT

class BodyMassIndexResultLineIndicator(context: Context, attrs: AttributeSet?) :
    View(context, attrs) {
    private val paint = Paint()

    @RequiresApi(Build.VERSION_CODES.M)
    private val colors =
        intArrayOf(
            resources.getColor(
                R.color.light_blue,
                null,
            ),
            resources.getColor(
                R.color.green,
                null,
            ),
            resources.getColor(
                R.color.orange,
                null,
            ),
            resources.getColor(
                R.color.red,
                null,
            ),
        )

    init {
        paint.style = FILL
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height

        var left = 0f

        for (color in colors.indices) {
            paint.color = colors[color]

            val right = left + TWENTY_FIVE_PERCENT * width
            canvas.drawRect(left, 0f, right, height.toFloat(), paint)

            left = right
        }
    }
}
