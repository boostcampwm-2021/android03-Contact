package com.ivyclub.contact.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.ivyclub.contact.util.dpToPixel
import com.ivyclub.contact.util.dpToPixelFloat

class CircleXImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private var bitmapPaint: Paint = Paint().apply {
        isAntiAlias = true
        color = backgroundTintList?.defaultColor ?: Color.BLACK
    }

    private val radius: Float by lazy {
        if (width > height) height / 2f else width / 2f
    }
    private val rectPaint: Paint by lazy {
        Paint().apply {
            color = Color.WHITE
            strokeWidth = 2.dpToPixelFloat
        }
    }
    private val startXY: Float by lazy {
        (radius * xVertexRatio).toFloat()
    }

    init {
        val defaultPadding = 2.dpToPixel
        setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding)
    }

    override fun onDraw(canvas: Canvas) {
        drawCircle(canvas)
        drawX(canvas)
        super.onDraw(canvas)
    }

    private fun drawCircle(canvas: Canvas) = with(canvas) {
        drawCircle(radius, radius, radius, bitmapPaint)
    }

    private fun drawX(canvas: Canvas) = with(canvas) {
        drawLine(startXY, startXY, width - startXY, height - startXY, rectPaint);
        drawLine(width - startXY, startXY, startXY, height - startXY, rectPaint)
    }

    companion object {
        private const val xVertexRatio = 0.484925
    }
}