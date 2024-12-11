package com.stone.architekt.objectdetector

import com.stone.architekt.imageprocessing.camera.CameraMode
import com.stone.architekt.imageprocessing.pipeline.ProcessingPipeline
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.DetailEnhanceStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.PerspectiveTransformStep
import com.stone.architekt.imageprocessing.steps.ShapeDetectionStep
import org.opencv.core.Mat

class ScanDocumentMode : CameraMode() {

    private val pipeline = ProcessingPipeline()
    private lateinit var detailEnhanceStep: DetailEnhanceStep
    private lateinit var grayscaleStep: GrayscaleStep
    private lateinit var blurStep: BlurStep
    private lateinit var edgeDetectionStep: EdgeDetectionStep
    private lateinit var shapeDetectionStep: ShapeDetectionStep
    private lateinit var perspectiveTransformStep: PerspectiveTransformStep

    private lateinit var boundingBoxFrameCaptured: Mat

    init {
        boundingBoxFrameCaptured = Mat()
        detailEnhanceStep =
            DetailEnhanceStep()
        grayscaleStep =
            GrayscaleStep()
        blurStep =
            BlurStep(5.0)
        edgeDetectionStep =
            EdgeDetectionStep()
        shapeDetectionStep =
            ShapeDetectionStep(boundingBoxFrameCaptured)
        perspectiveTransformStep =
            PerspectiveTransformStep(boundingBoxFrameCaptured)

        pipeline.addStep(detailEnhanceStep)
        pipeline.addStep(grayscaleStep)
        pipeline.addStep(blurStep)
        pipeline.addStep(edgeDetectionStep)
        pipeline.addStep(shapeDetectionStep)
        pipeline.addStep(perspectiveTransformStep)
    }

    override fun processCapturedFrame(image: Mat): Mat {
        if (!::boundingBoxFrameCaptured.isInitialized) {
            // Initialize boundingBoxFrameCaptured only when the first frame arrives
            boundingBoxFrameCaptured = image.clone()  // Clone the first frame
        } else {
            // For subsequent frames, you can reuse the Mat
            image.copyTo(boundingBoxFrameCaptured)
        }
        return pipeline.applyPipeline(image)
    }
}