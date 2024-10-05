package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class MorphologicalStep(
    private val kernelSize: Int = 5,
    private val iterations: Int = 1
) : ImageProcessingStep {

    override fun process(image: Mat): Mat {
        val kernel = Imgproc.getStructuringElement(
            Imgproc.MORPH_RECT,
            Size(kernelSize.toDouble(), kernelSize.toDouble())
        )
        val dilated = Mat()
        Imgproc.dilate(image, dilated, kernel, Point(-1.0, -1.0), iterations)

        val closed = Mat()
        Imgproc.morphologyEx(dilated, closed, Imgproc.MORPH_CLOSE, kernel)
        return closed
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "MorphologicalStep")
        jsonObject.put("kernelSize", kernelSize)
        jsonObject.put("iterations", iterations)
        return jsonObject
    }
}