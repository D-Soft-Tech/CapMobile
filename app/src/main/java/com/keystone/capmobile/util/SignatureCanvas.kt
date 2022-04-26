package com.keystone.capmobile.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.core.content.res.ResourcesCompat
import com.keystone.capmobile.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.abs
import android.graphics.Bitmap
import com.keystone.capmobile.util.AppConstants.bitmapToBase64


private const val STROKE_WIDTH = 3f

@Singleton
class SignatureCanvas @Inject constructor(@ApplicationContext context: Context) : View(context) {
    private val bgColor = ResourcesCompat.getColor(resources, R.color.white, null)
    private lateinit var myCanvas: Canvas
    private lateinit var bitmap: Bitmap
    private val penColor = ResourcesCompat.getColor(resources, R.color.black, null)
    private val paint = Paint().apply {
        color = penColor
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private var path = Path()
    private val touchTolerance = ViewConfiguration.get(context).scaledEdgeSlop
    private var motionX = 0f
    private var motionY = 0f
    private var currentX = 0f
    private var currentY = 0f

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (::bitmap.isInitialized) bitmap.recycle()

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        myCanvas = Canvas(bitmap)
        myCanvas.drawColor(bgColor)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(bitmap, 0f, 0f, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        motionX = event!!.x
        motionY = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> touchStart()
            MotionEvent.ACTION_MOVE -> touchMove()
            MotionEvent.ACTION_UP -> touchUp()
        }
        return true
    }

    private fun touchUp() {
        path.reset()
    }

    private fun touchMove() {
        val dx = abs(motionX - currentX)
        val dy = abs(motionX - currentY)
        if (dx >= touchTolerance || dy > touchTolerance) {
            path.quadTo(currentX, currentY, ((motionX + currentX)/2), ((motionY + currentY)/2))
            currentX = motionX
            currentY = motionY
            myCanvas.drawPath(path, paint)
        }
        invalidate()
    }

    private fun touchStart() {
        path.reset()
        path.moveTo(motionX, motionY)
        currentX = motionX
        currentY = motionY
    }

    fun clear() {
        bitmap.eraseColor(Color.TRANSPARENT)
        invalidate()
        System.gc()
    }

    fun getBitmap(): Bitmap? {
        isDrawingCacheEnabled = true
        buildDrawingCache()
        val bmp = Bitmap.createBitmap(drawingCache)
        isDrawingCacheEnabled = false
        return bmp
    }

    fun getSignatureInBase64() = getBitmap()?.let { bitmapToBase64(it) }
}