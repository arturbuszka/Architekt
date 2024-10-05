package com.stone.architekt.objectdetector

import com.stone.architekt.imageprocessing.camera.CameraMode
import com.stone.architekt.imageprocessing.pipeline.ProcessingPipeline
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.ShapeDetectionStep
import org.opencv.core.Mat

class LiveDetectionMode : CameraMode() {
    private val pipeline = ProcessingPipeline()
    private lateinit var grayscaleStep: GrayscaleStep
    private lateinit var blurStep: BlurStep
    private lateinit var edgeDetectionStep: EdgeDetectionStep
    private lateinit var shapeDetectionStep: ShapeDetectionStep

    private lateinit var boundingBoxFrameCaptured: Mat

    init {
        boundingBoxFrameCaptured = Mat()
        grayscaleStep =
            GrayscaleStep()
        blurStep =
            BlurStep(5.0)
        edgeDetectionStep =
            EdgeDetectionStep()
        shapeDetectionStep =
            ShapeDetectionStep(boundingBoxFrameCaptured)

        pipeline.addStep(grayscaleStep)
        pipeline.addStep(blurStep)
        pipeline.addStep(edgeDetectionStep)
        pipeline.addStep(shapeDetectionStep)
    }

    override fun processCapturedFrame(image: Mat): Mat {
        if (!::boundingBoxFrameCaptured.isInitialized) {
            // Initialize boundingBoxFrameCaptured only when the first frame arrives
            boundingBoxFrameCaptured = image.clone()  // Clone the first frame
        } else {
            // For subsequent frames, you can reuse the Mat
            image.copyTo(boundingBoxFrameCaptured)
        }
        pipeline.applyPipeline(image)
        return boundingBoxFrameCaptured
    }
}