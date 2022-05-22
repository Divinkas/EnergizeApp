package com.yatsenko.testhelper.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class ClickableImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val radius = 240.0f
    private var path: Path? = null
    private var rect: RectF? = null

    init {
        path = Path()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
        path?.addRoundRect(rect!!, radius, radius, Path.Direction.CW)
        path?.let { canvas.clipPath(it) }
        super.onDraw(canvas)
    }
}