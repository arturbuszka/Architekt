package com.stone.architekt.objectdetector

import android.graphics.Bitmap
import android.util.Log
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


fun convertMatToBitmap(mat: Mat): Bitmap {
    if (mat.empty()) {
        Log.d("objectdetector", "Mat is empty")
        throw IllegalArgumentException("Mat is empty")
    }
    val bitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888)
    Utils.matToBitmap(mat, bitmap)
    return bitmap
}

private fun boundingBox(frame: Mat): Mat {
    val gray = Mat()
    Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY)
    Imgproc.GaussianBlur(gray, gray, Size(5.0, 5.0), 0.0)
    val edges = Mat()
    Imgproc.Canny(gray, edges, 50.0, 150.0)
    val contours = mutableListOf<MatOfPoint>()
    val hierarchy = Mat()
    Imgproc.findContours(
        edges,
        contours,
        hierarchy,
        Imgproc.RETR_TREE,
        Imgproc.CHAIN_APPROX_SIMPLE
    )

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
                frame,
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
                frame, Point(boundRect.x.toDouble(), boundRect.y.toDouble()),
                Point(
                    boundRect.x + boundRect.width.toDouble(),
                    boundRect.y + boundRect.height.toDouble()
                ),
                Scalar(255.0, 0.0, 0.0), 2
            )
        }
    }
    return frame
}