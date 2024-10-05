package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class GrayscaleStep() : ImageProcessingStep {
    override fun process(image: Mat): Mat {
        val gray = Mat()
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)
        return gray
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "GrayscaleStep")
        return jsonObject
    }
}