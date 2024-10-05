package com.stone.architekt.view
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect
import android.util.AttributeSet
import android.view.SurfaceView
import org.opencv.android.Utils
import org.opencv.core.Mat
import android.util.Log

class MatImageView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs) {

    private var mCacheBitmap: Bitmap? = null
    private var mScale: Float = 0f
    private val TAG = "MatImageView"

    // Method to set a Mat and convert it to a Bitmap
    fun setImageMat(frame: Mat?) {
        frame?.let {
            try {
                // Convert Mat to Bitmap
                if (mCacheBitmap == null || mCacheBitmap!!.width != frame.width() || mCacheBitmap!!.height != frame.height()) {
                    mCacheBitmap = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888)
                }
                Utils.matToBitmap(frame, mCacheBitmap)

                // Trigger a redraw of the SurfaceView
                invalidate()  // Request a redraw
            } catch (e: Exception) {
                Log.e(TAG, "Error converting Mat to Bitmap: ${e.message}")
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mCacheBitmap?.let { bitmap ->
            // Clear the canvas
            canvas.drawColor(0, android.graphics.PorterDuff.Mode.CLEAR)

            if (mScale != 0f) {
                // Draw the Bitmap with scaling if required
                canvas.drawBitmap(
                    bitmap,
                    Rect(0, 0, bitmap.width, bitmap.height),
                    Rect(
                        ((canvas.width - mScale * bitmap.width) / 2).toInt(),
                        ((canvas.height - mScale * bitmap.height) / 2).toInt(),
                        ((canvas.width - mScale * bitmap.width) / 2 + mScale * bitmap.width).toInt(),
                        ((canvas.height - mScale * bitmap.height) / 2 + mScale * bitmap.height).toInt()
                    ),
                    null
                )
            } else {
                // Default to centering the bitmap
                canvas.drawBitmap(
                    bitmap,
                    Rect(0, 0, bitmap.width, bitmap.height),
                    Rect(
                        (canvas.width - bitmap.width) / 2,
                        (canvas.height - bitmap.height) / 2,
                        (canvas.width - bitmap.width) / 2 + bitmap.width,
                        (canvas.height - bitmap.height) / 2 + bitmap.height
                    ),
                    null
                )
            }
        }
    }

    fun setScale(scale: Float) {
        mScale = scale
    }
}
