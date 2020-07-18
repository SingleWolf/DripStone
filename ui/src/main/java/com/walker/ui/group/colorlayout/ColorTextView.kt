package com.walker.ui.group.colorlayout

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.INFINITE
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.appcompat.widget.AppCompatTextView
import kotlin.math.abs

class ColorTextView : AppCompatTextView {

    lateinit var paint: Paint
    lateinit var path: Path
    lateinit var progressAnimator: ObjectAnimator
    private var clipProgress: Float = 0F

    var textX: Float = 0F
    var baseLineY: Float = 0F
    var textY: Float = 0F
    var clipWidth: Float = 0F

    constructor(context: Context?) : this(context, null, 0)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initParam()
    }

    @SuppressLint("WrongConstant")
    private fun initParam() {
        paint = Paint()
        paint.run {
            isAntiAlias = true
            isDither = true
            textAlign = Paint.Align.CENTER
            textSize = this@ColorTextView.textSize
        }

        path = Path()
        progressAnimator = ObjectAnimator.ofFloat(this, "progress", 0F, 1F)
        progressAnimator.run {
            duration = 5 * 1000L
            interpolator = LinearInterpolator()
            repeatCount = INFINITE
        }
    }

    fun setProgress(progress: Float) {
        clipProgress = progress
        postInvalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        progressAnimator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        progressAnimator?.end()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.run {
            drawStep1(canvas)
            drawStep2(canvas)
        }
    }

    private fun drawStep1(canvas: Canvas) {
        canvas.save()
        paint.color = Color.RED
        paint.measureText(text.toString())
        textX = (paddingLeft + width / 2).toFloat()
        baseLineY = abs(paint.ascent() + paint.descent()) / 2
        textY = paddingTop + height / 2 + baseLineY
        clipWidth = clipProgress * width + paddingLeft
        //裁剪
        path.reset()
        path.addRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            clipWidth,
            height.toFloat() + paddingBottom,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        //绘制文本
        canvas.drawText(text.toString(), textX, textY, paint)
        canvas.restore()
    }

    private fun drawStep2(canvas: Canvas) {
        canvas.save()
        paint.color = Color.BLUE
        paint.measureText(text.toString())
        textX = (paddingLeft + width / 2).toFloat()
        baseLineY = abs(paint.ascent() + paint.descent()) / 2
        textY = paddingTop + height / 2 + baseLineY
        clipWidth = clipProgress * width + paddingLeft
        //裁剪
        path.reset()
        path.addRect(
            clipWidth,
            paddingTop.toFloat(),
            width.toFloat() + paddingRight,
            height.toFloat() + paddingBottom,
            Path.Direction.CW
        )
        canvas.clipPath(path)
        //绘制文本
        canvas.drawText(text.toString(), textX, textY, paint)
        canvas.restore()
    }
}