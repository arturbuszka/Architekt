package com.stone.architekt.objectdetector

import com.stone.architekt.imageprocessing.camera.CameraMode
import org.opencv.core.Mat

class ScanDocumentMode : CameraMode() {
    init {
    }

    override fun processCapturedFrame(image: Mat): Mat {
        return image
    }
}