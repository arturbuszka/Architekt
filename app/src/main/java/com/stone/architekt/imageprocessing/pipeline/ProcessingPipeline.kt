package com.stone.architekt.imageprocessing.pipeline

import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.DetailEnhanceStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.ShapeDetectionStep
import org.json.JSONException
import org.json.JSONObject
import org.opencv.core.Mat

class ProcessingPipeline {
    private val steps = mutableListOf<ImageProcessingStep>()

    fun addStep(step: ImageProcessingStep) {
        steps.add(step)
    }

    fun removeStep(step: Class<out ImageProcessingStep>) {
        steps.removeIf { it.javaClass == step }
    }

    fun applyPipeline(image: Mat): Mat {
        var processedImage = image.clone()
        for (step in steps) {
            processedImage = step.process(processedImage)
        }
        return processedImage
    }

    fun loadFromJson(jsonString: String) {
        try {
            val jsonObject = JSONObject(jsonString)
            val jsonSteps = jsonObject.getJSONArray("steps")

            // Clear existing steps before loading
            steps.clear()

            // Loop through each step in the JSON
            for (i in 0 until jsonSteps.length()) {
                val stepObject = jsonSteps.getJSONObject(i)
                val stepType = stepObject.getString("type")

                // Rebuild each step based on the "type" field
                when (stepType) {
                    "DetailEnhanceStep" -> {
                        val sigmaS = stepObject.getDouble("sigmaS")
                        val sigmaR = stepObject.getDouble("sigmaR")
                        val detailEnhanceStep = DetailEnhanceStep(sigmaS.toFloat(), sigmaR.toFloat())
                        addStep(detailEnhanceStep)
                    }
                    "BlurStep" -> {
                        val blurSize = stepObject.getDouble("size")
                        val blurStep = BlurStep(blurSize)
                        addStep(blurStep)
                    }
                    "EdgeDetectionStep" -> {
                        val edgeDetectionStep = EdgeDetectionStep()
                        addStep(edgeDetectionStep)
                    }
                    "ShapeDetectionStep" -> {
//                        val shapeDetectionStep = ShapeDetectionStep()
//                        addStep(shapeDetectionStep)
                    }
                    // Add more steps here as needed for other step types
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace() // Handle JSON parsing errors
        }
    }


}