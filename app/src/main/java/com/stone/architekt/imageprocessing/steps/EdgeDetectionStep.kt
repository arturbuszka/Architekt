package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

class EdgeDetectionStep(
) : ImageProcessingStep {
    override fun process(image: Mat): Mat {
        val edges = Mat()
        Imgproc.Canny(image, edges, 50.0, 150.0)
        return edges
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "EdgeDetectionStep")
        return jsonObject
    }
}