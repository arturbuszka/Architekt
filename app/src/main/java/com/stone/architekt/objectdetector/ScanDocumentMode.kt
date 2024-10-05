package com.stone.architekt.objectdetector

import com.stone.architekt.imageprocessing.camera.CameraMode
import com.stone.architekt.imageprocessing.pipeline.ProcessingPipeline
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.ShapeDetectionStep
import org.opencv.core.Mat

class ScanDocumentMode : CameraMode() {
    init {
    }

    override fun processCapturedFrame(image: Mat): Mat {

        return image
    }
}