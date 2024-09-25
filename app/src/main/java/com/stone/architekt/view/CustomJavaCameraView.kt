package com.stone.architekt.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import org.opencv.android.JavaCameraView

class CustomJavaCameraView(context: Context, attrs: AttributeSet?) : JavaCameraView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val screenWidth = ViewGroup.getDefaultSize(suggestedMinimumWidth, widthMeasureSpec)
        val screenHeight = ViewGroup.getDefaultSize(suggestedMinimumHeight, heightMeasureSpec)

        if (mFrameWidth > 0 && mFrameHeight > 0) {
            val aspectRatio = mFrameWidth.toDouble() / mFrameHeight

            val calculatedWidth: Int
            val calculatedHeight: Int

            if (screenWidth.toDouble() / screenHeight > aspectRatio) {
                calculatedWidth = screenWidth
                calculatedHeight = (screenWidth / aspectRatio).toInt()
            } else {
                calculatedHeight = screenHeight
                calculatedWidth = (screenHeight * aspectRatio).toInt()
            }
            setMeasuredDimension(calculatedWidth, calculatedHeight)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}