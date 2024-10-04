package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import org.opencv.photo.Photo

class ShapeClassificationStep(private val originalFrame: Mat, private val contourStep: ShapeDetectionStep
) : ImageProcessingStep {
    override fun process(image: Mat): Mat {
        val contours = contourStep.getContours()
        // Loop through each contour and detect shapes
        for (contour in contours) {
            // Approximate contours to polygons + get bounding rects
            val approxCurve = MatOfPoint2f()
            val contour2f = MatOfPoint2f(*contour.toArray())
            Imgproc.approxPolyDP(
                contour2f,
                approxCurve,
                Imgproc.arcLength(contour2f, true) * 0.02,
                true
            )
            val points = MatOfPoint(*approxCurve.toArray())

            val boundRect = Imgproc.boundingRect(points)

            // Detect if it's a rectangle (notebook) or circle (can)
            val aspectRatio = boundRect.width.toDouble() / boundRect.height.toDouble()
            if (aspectRatio >= 0.8 && aspectRatio <= 1.2 && points.total() >= 8) {
                // Circle detected (like a can)
                Imgproc.circle(
                    originalFrame,
                    Point(
                        boundRect.x + boundRect.width / 2.0,
                        boundRect.y + boundRect.height / 2.0
                    ),
                    Math.min(boundRect.width, boundRect.height) / 2,
                    Scalar(0.0, 255.0, 0.0),
                    2
                )
            } else if (points.total() == 4L) {
                // Rectangle detected (like a notebook)
                Imgproc.rectangle(
                    originalFrame, Point(boundRect.x.toDouble(), boundRect.y.toDouble()),
                    Point(
                        boundRect.x + boundRect.width.toDouble(),
                        boundRect.y + boundRect.height.toDouble()
                    ),
                    Scalar(255.0, 0.0, 0.0), 2
                )
            }
        }
        return originalFrame
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        return jsonObject
    }
}