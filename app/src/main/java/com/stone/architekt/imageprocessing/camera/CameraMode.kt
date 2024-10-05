package com.stone.architekt.imageprocessing.camera

import org.opencv.core.Mat

abstract class CameraMode {
    abstract fun processCapturedFrame(image: Mat): Mat
}