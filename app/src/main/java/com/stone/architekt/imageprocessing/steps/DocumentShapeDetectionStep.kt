package com.stone.architekt.imageprocessing.steps

import android.util.Log
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc

class DocumentShapeDetectionStep(
    private val originalFrame: Mat
) : ImageProcessingStep {


    override fun process(image: Mat): Mat {
        return originalFrame
    }


    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        return jsonObject
    }
}