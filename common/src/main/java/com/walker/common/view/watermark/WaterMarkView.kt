package com.walker.common.view.watermark

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import kotlin.math.sqrt

class WaterMarkView(

    private val context: Context,

    private val text: String,

    private val angle: Int,

    private val fontSize: Int

) : Drawable() {

    private var mPaint: Paint = Paint()

    private var bitmapWaterMark: Bitmap? = null

    private var markWaterCanvas: Canvas? = null

    //文字得宽高
    private var textH: Float = 0F

    private var textW: Float = 0F

    //图片得宽高
    private var bitmapW: Float = 0F

    private var bitmapH: Float = 0F

    //水印得位置
    private var watermarkW: Int = 0

    private var watermarkH: Int = 0

    init {

        mPaint.style = Paint.Style.FILL_AND_STROKE
        mPaint.color = Color.GRAY

    }

    override fun draw(canvas: Canvas) {
        mPaint.textSize = sp2px(context, fontSize.toFloat()).toFloat()
        //文字宽高
        textW = mPaint.measureText(text)

        textH = mPaint.fontMetrics.bottom - mPaint.fontMetrics.top

        //底层图片宽高
        bitmapW = bounds.width().toFloat()

        bitmapH = bounds.height().toFloat()

        //算出水印的宽高
        val sqr = sqrt((bitmapW * bitmapW + bitmapH * bitmapH))

        watermarkW = sqr.toInt()

        watermarkH = sqr.toInt()

        //水印画布
        bitmapWaterMark = Bitmap.createBitmap(

            watermarkW, watermarkH, Bitmap.Config.ARGB_8888

        )

        markWaterCanvas = Canvas(bitmapWaterMark!!)

        var ss = sqrt(2.toFloat()) / 2

        markWaterCanvas?.translate(
            -((sqrt(2 * watermarkH * watermarkH.toFloat())) / 2 - watermarkH / 2) * ss,

            -((sqrt(2 * watermarkH * watermarkH.toFloat())) / 2 - watermarkH / 2) * ss
        )

        //旋转
        markWaterCanvas?.rotate(

            angle.toFloat(), (watermarkW / 2).toFloat(),

            (watermarkW / 2).toFloat()

        )

        //根据画布大小添加水印文字
        val textLength = textW + 80

        val textColumn = textH + 120

        val row: Int = (watermarkW / textLength).toInt()

        val column: Int = (watermarkH / textColumn).toInt()

        for (i in 0..row + 1) {

            var count = 0

            for (j in 0..column + 2) {

                count++

                if (count % 2 == 0) {

                    markWaterCanvas?.drawText(

                        text,

                        textLength * i,

                        textColumn * j,

                        mPaint,

                        )

                } else {

                    //偶数行进行错开
                    markWaterCanvas?.drawText(

                        text,

                        textLength * i + (textW / 2),

                        textColumn * j,

                        mPaint,

                        )

                }

            }

        }

        if (bitmapWaterMark != null) {
            canvas?.drawBitmap(bitmapWaterMark!!, 0f, 0f, mPaint)
        }

    }

    override fun setAlpha(alpha: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {

        return PixelFormat.UNKNOWN

    }

    companion object {

        fun sp2px(context: Context, spValue: Float): Int {

            val fontScale = context.resources.displayMetrics.scaledDensity

            return (spValue * fontScale + 0.5f).toInt()

        }

    }

}
