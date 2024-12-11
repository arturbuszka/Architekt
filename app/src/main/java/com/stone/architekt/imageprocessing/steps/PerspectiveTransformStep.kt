package com.stone.architekt.imageprocessing.steps

import android.util.Log
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.imgproc.Imgproc
import org.opencv.utils.Converters

class PerspectiveTransformStep(
    private val originalFrame: Mat
) : ImageProcessingStep {

    private var documentCorners: List<Point> = emptyList()

    fun setDocumentsCorners(corners: List<Point>) {
        documentCorners = corners
    }


    override fun process(image: Mat): Mat {
        if (documentCorners.size == 4) {
            val srcPoints = Converters.vector_Point2f_to_Mat(documentCorners)
            val outputPoints = Converters.vector_Point2f_to_Mat(
                listOf(
                    Point(0.0, 0.0),
                    Point(image.width().toDouble(), image.height().toDouble()),
                    Point(0.0, image.height().toDouble()),
                )
            )

            val transformationMatrix = Imgproc.getPerspectiveTransform(srcPoints, outputPoints)
            val transformed = Mat()
            Imgproc.warpPerspective(originalFrame, transformed, transformationMatrix, image.size())
            return transformed
        }

        return image
    }


    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        return jsonObject
    }
}