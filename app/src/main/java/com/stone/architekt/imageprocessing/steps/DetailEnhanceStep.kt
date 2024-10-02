package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.photo.Photo

class DetailEnhanceStep(
    private val sigmaS: Float = 20.0f,
    private val sigmaR: Float = 0.15f
) : ImageProcessingStep {
    override fun process(image: Mat): Mat {
        val enhanced = Mat()
        Photo.detailEnhance(image, enhanced, sigmaS, sigmaR)
        return enhanced
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "DetailEnhanceStep")
        jsonObject.put("sigmaS", sigmaS)
        jsonObject.put("sigmaR", sigmaR)
        return jsonObject
    }
}