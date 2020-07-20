package com.walker.ui.group.goodfish

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import com.walker.core.log.LogHelper
import kotlin.math.acos
import kotlin.math.atan2
import kotlin.math.sqrt

/**
 * @Author Walker
 *
 * @Date   2020-07-20 22:30
 *
 * @Summary 可点击游动的锦鲤
 */
class GoodFishView : RelativeLayout {
    companion object {
        const val TAG = "GoodFishView"
    }

    var paint: Paint = Paint()
    var path: Path = Path()
    var ivFish: ImageView? = null
    var fishDrawable: GoodFishDrawable? = null
    lateinit var rippleAnnotator: ObjectAnimator

    var touchX: Float = 0F
    var touchY: Float = 0F
    var ripple: Float = 0F
    var circleAlpha: Int = 100

    var fishRelativeCorePoint: PointF? = null
    var fishAbsoluteCorePoint: PointF? = null
    var fishAbsoluteHeadPoint: PointF? = null
    var touchPoint: PointF? = null
    var controlAngle: Float = 0F
    var controlDelta: Float = 0F
    var controlPoint: PointF? = null
    var ivFishXYAnimator: ObjectAnimator? = null

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        setWillNotDraw(false)

        paint.run {
            isDither = true
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = 8F
            color = Color.BLUE
        }

        ivFish = ImageView(context)
        ivFish?.run {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            fishDrawable = GoodFishDrawable()
            setImageDrawable(fishDrawable!!)
            addView(ivFish)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("ObjectAnimatorBinding")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            touchX = this.x
            touchY = this.y
            LogHelper.get().i(TAG, "onTouchEvent:($touchX,$touchY)")
            rippleAnnotator = ObjectAnimator.ofFloat(this@GoodFishView, "rippleValue", 0F, 1F)
            rippleAnnotator.run {
                duration = 1000L
                start()
            }
            makeTrail()
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //绘制圆波纹
        canvas?.run {
            paint.alpha = circleAlpha
            drawCircle(touchX, touchY, ripple * 150, paint)
        }
    }

    fun setRippleValue(v: Float) {
        ripple = v
        circleAlpha = (100 * (1 - ripple)).toInt()
        invalidate()
    }

    fun calculateAngleByPoints(O: PointF, A: PointF, B: PointF): Float {
        // cosAOB
        // OA*OB=(Ax-Ox)(Bx-Ox)+(Ay-Oy)*(By-Oy)
        val AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y)
        val OALength = sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y))
        // OB 的长度
        val OBLength = sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y))
        val cosAOB = AOB / (OALength * OBLength)

        // 反余弦
        val angleAOB = Math.toDegrees(acos(cosAOB).toDouble())
        // AB连线与X的夹角的tan值 - OB与x轴的夹角的tan值
        val direction = (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x)

        return if (direction == 0F) {
            if (AOB >= 0) {
                0F
            } else {
                180F
            }
        } else {
            if (direction > 0) {
                (-angleAOB).toFloat()
            } else {
                angleAOB.toFloat()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun makeTrail() {
        // 鱼的重心：相对ImageView坐标
        fishRelativeCorePoint = fishDrawable!!.corePoint
        // 鱼的重心：绝对坐标 --- 起始点
        fishAbsoluteCorePoint =
            PointF(ivFish!!.x + fishRelativeCorePoint!!.x, ivFish!!.y + fishRelativeCorePoint!!.y)
        // 鱼头圆心的坐标 -- 控制点1
        fishAbsoluteHeadPoint =
            PointF(
                ivFish!!.x + fishDrawable!!.fishHeadPoint.x,
                ivFish!!.y + fishDrawable!!.fishHeadPoint.y
            )
        // 点击坐标 -- 结束点
        touchPoint = PointF(touchX, touchY)
        // 控制点2 的坐标
        controlAngle =
            calculateAngleByPoints(fishAbsoluteCorePoint!!, fishAbsoluteHeadPoint!!, touchPoint!!)
        controlDelta = calculateAngleByPoints(
            fishAbsoluteCorePoint!!,
            PointF(fishAbsoluteCorePoint!!.x + 1F, fishAbsoluteCorePoint!!.y),
            fishAbsoluteHeadPoint!!
        )
        controlPoint = fishDrawable!!.calculatePoint(
            fishAbsoluteCorePoint!!,
            fishDrawable!!.headRadius * 1.6F,
            (controlAngle + controlDelta).toDouble()
        )

        //根据三阶贝塞尔曲线，绘制锦鲤游动轨迹
        path.reset()
        val relativeX = fishRelativeCorePoint!!.x
        val relativeY = fishRelativeCorePoint!!.y
        path.moveTo(fishAbsoluteCorePoint!!.x - relativeX, fishAbsoluteCorePoint!!.y - relativeY)
        path.cubicTo(
            fishAbsoluteCorePoint!!.x - relativeX, fishAbsoluteCorePoint!!.y - relativeY,
            controlPoint!!.x - relativeX, controlPoint!!.y - relativeY,
            touchPoint!!.x - relativeX, touchPoint!!.y - relativeY
        )
        ivFishXYAnimator = ObjectAnimator.ofFloat(ivFish!!, "x", "y", path)
        ivFishXYAnimator?.run {
            duration = 2500L
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation: Animator?) {
                    super.onAnimationStart(animation)
                    //加速锦鲤的游动速度
                    fishDrawable!!.accelerateCoefficient = 2F
                }

                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    //还原锦鲤的游动速度
                    fishDrawable!!.accelerateCoefficient = 1F
                }
            })

            val pathMeasure = PathMeasure(path, false)
            var tan = FloatArray(2)
            addUpdateListener {
                it?.run {
                    // 执行了整个周期的百分之多少
                    val fraction: Float = animatedFraction
                    pathMeasure.getPosTan(pathMeasure.length * fraction, null, tan)
                    //锦鲤调头的角度
                    val angle = Math.toDegrees(atan2(-tan[1], tan[0]).toDouble())
                    fishDrawable!!.fishAngle = angle
                }
            }
            start()
        }
    }
}