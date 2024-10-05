package com.stone.architekt.imageprocessing.pipeline

import org.json.JSONObject
import org.opencv.core.Mat

interface ImageProcessingStep {
    fun process(image: Mat) : Mat
    fun toJson(): JSONObject
}