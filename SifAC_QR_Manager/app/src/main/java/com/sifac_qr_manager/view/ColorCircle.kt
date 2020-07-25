package com.sifac_qr_manager.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.sifac_qr_manager.R

/**
 * TODO: document your custom view class.
 */
class ColorCircle : AppCompatImageView {

    var colorDefine: Int = 0

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val array = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.ColorCircle,
            0, 0)
        colorDefine = array.getColor(R.styleable.ColorCircle_color, Color.BLACK)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(Color.TRANSPARENT)

        val paint = Paint()
        paint.color = colorDefine
        paint.strokeWidth = 5f
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL

        val centerX = (width / 2).toFloat()
        val centerY = (height / 2).toFloat()
        canvas.drawCircle(centerX, centerY, centerX, paint)
    }
}