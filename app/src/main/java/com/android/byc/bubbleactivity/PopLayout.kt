package com.android.byc.bubbleactivity

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

/**
 * Created by yu on 2019/5/8
 */
@Suppress("DEPRECATION")
class PopLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr), View.OnLayoutChangeListener {

    private var radiusSize = 16

    private var triSize = 16

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var popRectPath: Path = Path()

    private var popTriPath: Path = Path()

    private var destTriPath: Path = Path()

    private var martrix: Matrix = Matrix()
    
    private val lines = LinesDraw(context)

    private inner class LinesDraw @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)
            mPaint.color = Color.WHITE
            canvas.drawLine(width / 2f, dip2px(10f).toFloat(), width / 2f,
                height.toFloat() - dip2px(10f).toFloat() , mPaint)
        }

    }

    init {
        initialize()
        addView(lines)
    }

    private fun initialize() {

        if (background == null) {
            setBackgroundColor(Color.BLACK)
        }
        mPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        resetBulge()
        resetMask()
        addOnLayoutChangeListener(this)
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    private fun resetBulge() {
        popTriPath.reset()
        popTriPath.lineTo((triSize * 2).toFloat(), 0f)
        popTriPath.lineTo(triSize.toFloat(), triSize.toFloat())
        popTriPath.close()
    }

    private fun resetMask() {
        popRectPath.reset()
        val width = measuredWidth
        val height = measuredHeight
        if (width <= radiusSize || height <= radiusSize) return
        val offset =  radiusSize * 7.5
        popRectPath.addRect(RectF(0f, 0f, dip2px(100f).toFloat(), dip2px(40f).toFloat()), Path.Direction.CW)
        popRectPath.addRoundRect(RectF(triSize.toFloat(), triSize.toFloat(),
            (width - triSize).toFloat(), (height - triSize).toFloat()), radiusSize.toFloat(), radiusSize.toFloat(), Path.Direction.CCW)
        popRectPath.fillType = Path.FillType.INVERSE_EVEN_ODD
        martrix.setTranslate((-triSize).toFloat(), 0f)
        popTriPath.transform(martrix, destTriPath)
        popRectPath.addPath(destTriPath, offset.toFloat(), (height - triSize).toFloat())
    }

    override fun draw(canvas: Canvas) {
        val layer = canvas.saveLayer(0f, 0f, width.toFloat(),
            height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        super.draw(canvas)
        canvas.drawPath(popRectPath, mPaint)
        canvas.restoreToCount(layer)
    }

    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int,
                                oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        resetMask()
        postInvalidate()
    }
}