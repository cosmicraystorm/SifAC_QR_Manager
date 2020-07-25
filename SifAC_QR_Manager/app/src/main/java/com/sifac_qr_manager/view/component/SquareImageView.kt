package com.sifac_qr_manager.view.component

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import com.sifac_qr_manager.R

/**
 * TODO: document your custom view class.
 */
class SquareImageView(context: Context) : androidx.appcompat.widget.AppCompatImageView(context, null) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }
}
