package com.stelmo.brtest

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.Drawable

class TextDrawable(val text: String) : Drawable() {
    private val paint: Paint

    init {

        this.paint = Paint()
        paint.color = Color.WHITE
        paint.textSize = 52f
        paint.isAntiAlias = true
        paint.isFakeBoldText = true
        paint.setShadowLayer(12f, 0f, 0f, Color.BLACK)
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.LEFT
    }

    override fun draw(canvas: Canvas) {
        val bounds = bounds
        //no text, but leaving for debugging
        //canvas.drawText(text, bounds.centerX() - 15f /*just a lazy attempt to centre the text*/ * text.length(), bounds.centerY() + 15f, paint);
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }
}