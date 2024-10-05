package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class BlurStep(
    private val size: Double = 5.0
) : ImageProcessingStep {
    override fun process(image: Mat): Mat {
        val blurred = Mat()
        Imgproc.GaussianBlur(image, blurred, Size(size, size), 0.0)
        return blurred
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "BlurStep")
        jsonObject.put("size", size)
        return jsonObject
    }
}