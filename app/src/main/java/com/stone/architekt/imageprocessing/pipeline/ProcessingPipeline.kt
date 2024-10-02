package com.stone.architekt.imageprocessing.pipeline

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

    }


}