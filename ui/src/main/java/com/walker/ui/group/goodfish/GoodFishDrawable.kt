package com.walker.ui.group.goodfish

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator
import kotlin.math.cos
import kotlin.math.sin

/**
 * @Author Walker
 *
 * @Date   2020-07-19 07:58
 *
 * @Summary 锦鲤绘制
 */
class GoodFishDrawable : Drawable(), Animatable {

    companion object {
        const val OTHER_ALPHA = 122
        const val DEFAULT_HEAD_RADIUS = 100F
    }

    lateinit var paint: Paint
    lateinit var path: Path
    lateinit var corePoint: PointF
    lateinit var angleAnimator: ObjectAnimator

    var fishAngle: Double = 0.0
    var headRadius: Float = DEFAULT_HEAD_RADIUS
    var bodyLength: Float = headRadius * 3.2F
    var findFinLength: Float = headRadius * 0.9F
    var finLength: Float = headRadius * 1.3F
    var largeCircleRadius: Float = headRadius * 0.7F
    var midCircleRadius: Float = headRadius * 0.42F
    var minCircleRadius: Float = headRadius * 0.168F
    var largeTrapeziumHeight: Float = headRadius * (0.7F + 0.42F)
    var smallTrapeziumHeight: Float = headRadius * (0.42F * (0.4F + 2.7F))

    init {
        paint = Paint()
        paint.run {
            style = Paint.Style.FILL
            isAntiAlias = true
            isDither = true
            setARGB(OTHER_ALPHA, 244, 92, 71)
        }

        path = Path()

        corePoint = PointF(4.19F * headRadius, 4.19F * headRadius)

        angleAnimator = ObjectAnimator.ofFloat(this, "fishAngle", 0F, 360F)
        angleAnimator.run {
            duration = 30 * 1000L
            interpolator = LinearInterpolator()
            repeatCount = ValueAnimator.INFINITE
        }
        angleAnimator.start()
    }

    override fun isRunning() = angleAnimator?.isRunning

    override fun start() {
        angleAnimator?.start()
    }

    override fun stop() {
        angleAnimator?.end()
    }

    fun setFishAngle(angle: Float) {
        fishAngle = angle.toDouble()
        invalidateSelf()
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
    }

    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        colorFilter?.let {
            paint.setColorFilter(it)
        }
    }

    override fun getIntrinsicWidth(): Int = (8.38F * headRadius).toInt()

    override fun getIntrinsicHeight(): Int = (8.38F * headRadius).toInt()

    /**
     * 核心算法，根据角度、长度利用正余弦计算目标点
     */
    fun calculatePoint(startPoint: PointF, length: Float, angle: Double): PointF {
        //角度转换为弧度
        val degrees = Math.toRadians(angle)
        //利用余弦求x坐标
        val deltaX: Float = (cos(degrees) * length).toFloat()
        //利用正弦求y坐标
        val deltaY: Float = -(sin(degrees) * length).toFloat()
        return PointF(startPoint.x + deltaX, startPoint.y + deltaY)
    }


    override fun draw(canvas: Canvas) {
        val headPoint = drawHead(canvas)
        drawFin(canvas, headPoint)
        val largeCirclePoint = drawBodyPart(canvas)
        drawMainBody(canvas, headPoint, largeCirclePoint)
    }

    /**
     * 绘制头部
     */
    private fun drawHead(canvas: Canvas): PointF {
        val headPoint: PointF = calculatePoint(corePoint, bodyLength / 2, fishAngle)
        canvas.drawCircle(headPoint.x, headPoint.y, headRadius, paint)
        return headPoint
    }

    /**
     * 绘制鱼鳍
     */
    private fun drawFin(canvas: Canvas, headPoint: PointF) {
        //右测
        val rightFinPoint = calculatePoint(headPoint, findFinLength, fishAngle - 110)
        makeFins(canvas, rightFinPoint, fishAngle, true)
        //左侧
        val leftFinPoint = calculatePoint(headPoint, findFinLength, fishAngle + 110)
        makeFins(canvas, leftFinPoint, fishAngle, false)
    }

    private fun makeFins(
        canvas: Canvas,
        startPoint: PointF,
        fishAngle: Double,
        isRight: Boolean
    ) {
        val controlAngle = 115.0
        //鱼鳍的终点
        val endPoint = calculatePoint(startPoint, finLength, fishAngle - 180)
        //控制点
        val calculateAngle = if (isRight) {
            fishAngle - controlAngle
        } else {
            fishAngle + controlAngle
        }
        val controlPoint = calculatePoint(startPoint, finLength * 1.8F, calculateAngle)
        //绘制
        path.reset()
        path.moveTo(startPoint.x, startPoint.y)
        path.quadTo(controlPoint.x, controlPoint.y, endPoint.x, endPoint.y)
        canvas.drawPath(path, paint)
    }

    /**
     * 绘制下体
     */
    private fun drawBodyPart(canvas: Canvas): PointF {
        //绘制下体第一部分
        val largeCirclePoint = calculatePoint(corePoint, bodyLength / 2, fishAngle - 180)
        val midCirclePoint = makeTrapeziumAndCircle(
            canvas,
            largeCirclePoint,
            largeTrapeziumHeight,
            largeCircleRadius,
            midCircleRadius, true
        )
        //绘制下体第二部分
        makeTrapeziumAndCircle(
            canvas,
            midCirclePoint,
            smallTrapeziumHeight,
            midCircleRadius,
            minCircleRadius, false
        )
        //绘制尾巴
        makeTails(canvas, midCirclePoint, smallTrapeziumHeight)
        return largeCirclePoint
    }

    private fun makeTrapeziumAndCircle(
        canvas: Canvas,
        largeCirclePoint: PointF,
        trapeziumHeight: Float,
        lCircleRadius: Float,
        sCircleRadius: Float,
        isDrawLargeCircle: Boolean
    ): PointF {
        val smallCirclePoint = calculatePoint(largeCirclePoint, trapeziumHeight, fishAngle - 180)
        if (isDrawLargeCircle) {
            //绘制大圆
            canvas.drawCircle(largeCirclePoint.x, largeCirclePoint.y, lCircleRadius, paint)
        }
        //绘制小圆
        canvas.drawCircle(smallCirclePoint.x, smallCirclePoint.y, sCircleRadius, paint)
        //绘制梯形
        val tlPoint = calculatePoint(largeCirclePoint, lCircleRadius, fishAngle + 90)
        val trPoint = calculatePoint(largeCirclePoint, lCircleRadius, fishAngle - 90)
        val blPoint = calculatePoint(smallCirclePoint, sCircleRadius, fishAngle + 90)
        val brPoint = calculatePoint(smallCirclePoint, sCircleRadius, fishAngle - 90)
        path.reset()
        path.moveTo(tlPoint.x, tlPoint.y)
        path.lineTo(trPoint.x, trPoint.y)
        path.lineTo(brPoint.x, brPoint.y)
        path.lineTo(blPoint.x, blPoint.y)
        canvas.drawPath(path, paint)

        return smallCirclePoint
    }

    /**
     * 绘制尾巴
     */
    private fun makeTails(canvas: Canvas, topPoint: PointF, triangleLength: Float) {
        //绘制第一个三角形
        makeTriangle(canvas, topPoint, triangleLength)
        //绘制第二个三角形（相对小一些，可叠加）
        makeTriangle(canvas, topPoint, triangleLength * 0.8F)
    }

    private fun makeTriangle(canvas: Canvas, topPoint: PointF, triangleLength: Float) {
        val leftPoint = calculatePoint(topPoint, triangleLength, fishAngle + 150)
        val rightPoint = calculatePoint(topPoint, triangleLength, fishAngle - 150)
        path.reset()
        path.moveTo(topPoint.x, topPoint.y)
        path.lineTo(leftPoint.x, leftPoint.y)
        path.lineTo(rightPoint.x, rightPoint.y)
        canvas.drawPath(path, paint)
    }

    /**
     * 绘制主体
     */
    private fun drawMainBody(
        canvas: Canvas,
        headPoint: PointF,
        largeCirclePoint: PointF
    ) {
        val tlPoint = calculatePoint(headPoint, headRadius, fishAngle + 90)
        val trPoint = calculatePoint(headPoint, headRadius, fishAngle - 90)
        val blPoint = calculatePoint(largeCirclePoint, largeCircleRadius, fishAngle + 90)
        val brPoint = calculatePoint(largeCirclePoint, largeCircleRadius, fishAngle - 90)
        val controlLeftPoint = calculatePoint(headPoint, bodyLength * 0.56F, fishAngle + 130)
        val controlRightPoint = calculatePoint(headPoint, bodyLength * 0.56F, fishAngle - 130)
        path.reset()
        path.moveTo(tlPoint.x, tlPoint.y)
        path.quadTo(controlLeftPoint.x, controlLeftPoint.y, blPoint.x, blPoint.y)
        path.lineTo(brPoint.x, brPoint.y)
        path.quadTo(controlRightPoint.x, controlRightPoint.y, trPoint.x, trPoint.y)
        canvas.drawPath(path, paint)
    }
}