package com.stone.architekt.imageprocessing.steps

import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.json.JSONObject
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

class DocumentContourStep : ImageProcessingStep {

    private var fourPointsOrig: Array<Point>? = null

    override fun process(image: Mat): Mat {
        val gray = Mat()
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY)

        val contours = ArrayList<MatOfPoint>()
        Imgproc.findContours(gray, contours, Mat(), Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE)

        val sortedContours = contours.sortedByDescending { Imgproc.contourArea(it) }
        for (contour in sortedContours) {
            val peri = Imgproc.arcLength(MatOfPoint2f(*contour.toArray()), true)
            val approx = MatOfPoint2f()
            Imgproc.approxPolyDP(MatOfPoint2f(*contour.toArray()), approx, 0.02 * peri, true)

            if (approx.total() == 4L) {
                fourPointsOrig = approx.toArray()
                break
            }
        }

        if (fourPointsOrig != null) {
            return performPerspectiveTransform(image, fourPointsOrig!!)
        }
        return image
    }

    private fun performPerspectiveTransform(image: Mat, points: Array<Point>): Mat {
        val srcPoints = Mat(points.size, 2, Imgproc.CV_WARP_FILL_OUTLIERS)
        val dstPoints = Mat(4, 2, Imgproc.CV_WARP_FILL_OUTLIERS)

        val matrix = Imgproc.getPerspectiveTransform(srcPoints, dstPoints)
        val warped = Mat()
        Imgproc.warpPerspective(
            image,
            warped,
            matrix,
            Size(image.cols().toDouble(), image.rows().toDouble())
        )
        return warped
    }

    override fun toJson(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject.put("type", "DocumentContourStep")
        return jsonObject
    }
}