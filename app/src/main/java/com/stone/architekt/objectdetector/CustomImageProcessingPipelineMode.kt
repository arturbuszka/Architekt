package com.stone.architekt.objectdetector

import com.stone.architekt.imageprocessing.camera.CameraMode
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import com.stone.architekt.imageprocessing.pipeline.ProcessingPipeline
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.DetailEnhanceStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.PerspectiveTransformStep
import com.stone.architekt.imageprocessing.steps.ShapeDetectionStep
import org.opencv.core.Mat

class CustomImageProcessingPipelineMode : CameraMode() {

    private val pipeline = ProcessingPipeline()
    private var steps: MutableList<ImageProcessingStep> = mutableListOf()

    init {

    }

    override fun processCapturedFrame(image: Mat): Mat {
//        if (!::boundingBoxFrameCaptured.isInitialized) {
//            // Initialize boundingBoxFrameCaptured only when the first frame arrives
//            boundingBoxFrameCaptured = image.clone()  // Clone the first frame
//        } else {
//            // For subsequent frames, you can reuse the Mat
//            image.copyTo(boundingBoxFrameCaptured)
//        }
        return pipeline.applyPipeline(image)
    }

    fun getSteps(): List<ImageProcessingStep> {
        return steps
    }

    fun addStep(step: ImageProcessingStep) {
        steps.add(step)
        pipeline.addStep(step)
    }

    fun removeStep(step: ImageProcessingStep) {
        steps.remove(step)
        pipeline.removeStep(step::class.java)
    }

    fun clearSteps() {
        steps.clear()
        pipeline.clearSteps()
    }


}