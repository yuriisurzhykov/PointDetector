package com.yuriisurzhykov.pointsdetector.uicomponents.recycler

import android.animation.LayoutTransition
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.yuriisurzhykov.pointsdetector.uicomponents.R
import android.graphics.Bitmap
import android.util.Log


class RecyclerViewHolderCornerView : ConstraintLayout {

    private var isInDragState: Boolean = false
    private var cornerRadius: Float = 0f
    private var maskPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)
    private var maskBitmap: Bitmap? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWithSpecifications(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWithSpecifications(context, attrs, defStyleAttr)
    }

    init {
        maskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        setWillNotDraw(false)
        layoutTransition = LayoutTransition().apply {
            disableTransitionType(LayoutTransition.DISAPPEARING)
        }
    }

    private fun initWithSpecifications(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) {
        context.obtainStyledAttributes(attrs, R.styleable.RecyclerViewHolderCornerView, defStyleAttr, 0).run {
            cornerRadius =
                getDimensionPixelSize(
                    R.styleable.RecyclerViewHolderCornerView_viewHolderSwipeCorner,
                    0
                ).toFloat()
            recycle()
        }
    }

    override fun onDraw(canvas: Canvas) {
        Log.e("TAG", "onDraw: $isInDragState")
        if (isInDragState) {
            drawWhenSwipe(canvas)
        } else {
            super.onDraw(canvas)
        }
    }

    fun setInDragState(inDrag: Boolean) {
        isInDragState = inDrag
        invalidate()
    }

    private fun createMask(width: Int, height: Int): Bitmap? {
        val mask = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        val canvas = Canvas(mask)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.WHITE
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        canvas.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            cornerRadius,
            cornerRadius,
            paint
        )
        return mask
    }

    @SuppressLint("WrongCall")
    private fun drawWhenSwipe(canvas: Canvas) {
        val offscreenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val offscreenCanvas = Canvas(offscreenBitmap)
        super.onDraw(canvas)
        if (maskBitmap == null) {
            maskBitmap = createMask(width, height)
        }

        offscreenCanvas.drawBitmap(maskBitmap!!, 0f, 0f, maskPaint)
        canvas.drawBitmap(offscreenBitmap, 0f, 0f, paint)
    }
}