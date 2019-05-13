package com.android.byc.bubbleactivity

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by yu on 2019/5/10
 */
class MenuLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr), View.OnClickListener {


    private var paint = Paint()
    private val arrowHeight = dip2px(7f)
    private val path = Path()

    var textList = emptyList<String>()
        set(value) {
            field = value
            insertText()
        }
    var onClickListener: OnClickListener? = null

    private fun insertText() {
        removeAllViews()
        for (i in 0 until textList.size) {
            addView(getTextView(textList[i]))
        }
        requestLayout()
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var contentWidth = 0
        var contentHeight = 0
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.measure(widthMeasureSpec, heightMeasureSpec)
            contentWidth += child.measuredWidth
            contentHeight = Math.max(child.measuredHeight,contentHeight)
        }
        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(contentWidth, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(contentHeight + arrowHeight, MeasureSpec.EXACTLY)
        )
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var textHeight = 0
        var usedWidth = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            view.layout(usedWidth, 0, usedWidth + view.measuredWidth, view.measuredHeight)
            if (textHeight == 0) textHeight = view.measuredHeight
            usedWidth += view.measuredWidth
        }
    }

    private fun dip2px(dpValue: Float): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    override fun dispatchDraw(canvas: Canvas) {
        paint.color = Color.parseColor("#282B32")
        canvas.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat() - arrowHeight),
            dip2px(5f).toFloat(),
            dip2px(5f).toFloat(),
            paint
        )
        path.moveTo(width / 2f - arrowHeight, height.toFloat() - arrowHeight)
        path.lineTo(width / 2f + arrowHeight, height.toFloat() - arrowHeight)
        path.lineTo(width / 2f, height.toFloat())
        path.close()
        canvas.drawPath(path, paint)

        var usedWidth = 0
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            usedWidth += view.measuredWidth
            paint.color = Color.parseColor("#7D8082")
            canvas.drawLine(usedWidth.toFloat(), dip2px(9f).toFloat(), usedWidth.toFloat(), dip2px(30f).toFloat(), paint)
        }
        //这行代码写在后面就是子view在父view后绘制， 否则子view会遮住父view
        super.dispatchDraw(canvas)
    }

    private fun getTextView(textStr: String): TextView {
        val textView = TextView(context)
        textView.apply {
            text = textStr
            gravity = Gravity.CENTER
            setPadding(dip2px(12f), dip2px(10f), dip2px(12f), dip2px(8f))
            textSize = dip2px(4f).toFloat()
            setTextColor(Color.WHITE)
            layoutParams = ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            tag = textStr
            setOnClickListener(this@MenuLayout)
        }
        return textView
    }

    override fun onClick(v: View?) {
        if (textList.contains(v?.tag)) {
            onClickListener?.onClick(textList.indexOf(v?.tag))
        }
    }

    interface OnClickListener {
        fun onClick(index: Int)
    }
}