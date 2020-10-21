package com.cdek.courier.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import com.cdek.courier.data.models.paint.PaintPath
import java.util.*

class PaintView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) : View(context, attrs) {
    private var mX = 0f
    private var mY = 0f
    private var mPath: Path? = null
    private val mPaint: Paint
    private val paths = ArrayList<PaintPath>()
    private var currentColor = 0
    private var backgroundColor: Int? = DEFAULT_BG_COLOR
    private var strokeWidth = 0
    private val emboss = false
    private val mEmboss: MaskFilter
    private lateinit var mBitmap: Bitmap
    private var mCanvas: Canvas? = null
    private val mBitmapPaint =
        Paint(Paint.DITHER_FLAG)

    fun getmCanvas(): Canvas? {
        return mCanvas
    }

    fun init(metrics: DisplayMetrics) {
        val height = metrics.heightPixels
        val width = metrics.widthPixels
        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        currentColor = DEFAULT_COLOR
        strokeWidth = BRUSH_SIZE
    }

    fun clear() {
        backgroundColor = DEFAULT_BG_COLOR
        paths.clear()
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.save()
        mCanvas!!.drawColor(DEFAULT_BG_COLOR)
        for (paintPath in paths) {
            mPaint.color = paintPath.color
            mPaint.strokeWidth = paintPath.strokeWidth.toFloat()
            mPaint.maskFilter = null
            mPaint.maskFilter = mEmboss
            mCanvas!!.drawPath(paintPath.path, mPaint)
        }
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
        canvas.restore()
    }

    private fun touchStart(x: Float, y: Float) {
        mPath = Path()
        val paintPath = PaintPath(currentColor, true, strokeWidth, mPath)
        paths.add(paintPath)
        mPath!!.reset()
        mPath!!.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath!!.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2)
            mX = x
            mY = y
        }
    }

    private fun touchUp() {
        mPath!!.lineTo(mX, mY)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    companion object {
        var BRUSH_SIZE = 10
        const val DEFAULT_COLOR = Color.BLACK
        const val DEFAULT_BG_COLOR = Color.WHITE
        private const val TOUCH_TOLERANCE = 4f
    }

    init {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.color = DEFAULT_COLOR
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeJoin = Paint.Join.ROUND
        mPaint.strokeCap = Paint.Cap.ROUND
        mPaint.xfermode = null
        mPaint.alpha = 0xff
        mEmboss = EmbossMaskFilter(floatArrayOf(1f, 1f, 1f), 0.4f, 6F, 3.5f)
    }
}