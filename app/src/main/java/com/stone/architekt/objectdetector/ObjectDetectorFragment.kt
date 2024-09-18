package com.stone.architekt.objectdetector

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.Manifest
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraExtensionCharacteristics
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.stone.architekt.R
import com.stone.architekt.databinding.FragmentObjectdetectorBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.MatOfPoint
import org.opencv.core.MatOfPoint2f
import org.opencv.core.Point
import org.opencv.core.Scalar
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc


class ObjectDetectorFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {
//    private lateinit var viewModel: ObjectDetectorViewModel
    private lateinit var cameraView: CameraBridgeViewBase
    private lateinit var mRgba: Mat
    private lateinit var mHsv: Mat
    private lateinit var mMask: Mat

    // Initialize permission launcher
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.e("Camera Permission", "Permission granted")
            cameraView.enableView()  // Enable camera view after permission is granted
        } else {
            Log.e("Camera Permission", "Permission denied")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentObjectdetectorBinding.inflate(inflater)
        binding.lifecycleOwner = this
//        viewModel = ViewModelProviders.of(this).get(ObjectDetectorViewModel::class.java)
//        binding.viewModel = viewModel

        // Initialize OpenCV
        if (!OpenCVLoader.initLocal()) {
            Log.e("OpenCV", "Unable to load OpenCV")
        } else {
            Log.d("OpenCV", "OpenCV loaded successfully")
        }
        cameraView = binding.cameraView
        cameraView.visibility = View.VISIBLE
        cameraView.setCameraIndex(CameraCharacteristics.LENS_FACING_FRONT)

        cameraView.setCvCameraViewListener(this)
        Log.d("Camera", "set camera listener")
//        viewModel.eventGallerySelected.observe(viewLifecycleOwner, Observer { notifed ->
//        })

        requestCameraPermission()


        return binding.root

    }

    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission already granted
                cameraView.setCameraPermissionGranted();
                cameraView.enableView()
                Log.e("Camera Permission", "Camera view enabled ")
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // You can show some rationale to the user
                Log.e("Camera Permission", "Camera permission is required")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                // Request the permission
                Log.e("Camera Permission", "Camera permission request")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.e("Camera", "Camera view started")
        mRgba =  Mat(height, width, CvType.CV_8UC4)
        mHsv = Mat(height, width, CvType.CV_8UC3)
        mMask = Mat(height, width, CvType.CV_8UC1)
    }

    override fun onCameraViewStopped() {
        mRgba.release()
        mHsv.release()
        mMask.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        mRgba = inputFrame!!.rgba()
        // Convert the frame to grayscale
        val gray = Mat()
        Imgproc.cvtColor(mRgba, gray, Imgproc.COLOR_BGR2GRAY)

        // Blur the frame to reduce noise
        Imgproc.GaussianBlur(gray, gray, Size(5.0, 5.0), 0.0)

        // Edge detection using Canny
        val edges = Mat()
        Imgproc.Canny(gray, edges, 50.0, 150.0)

        // Find contours in the edges
        val contours = mutableListOf<MatOfPoint>()
        val hierarchy = Mat()
        Imgproc.findContours(edges, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE)

        // Iterate over each contour to detect shapes
        for (contour in contours) {
            // Approximate contours to polygons + get bounding rects
            val approxCurve = MatOfPoint2f()
            val contour2f = MatOfPoint2f(*contour.toArray())
            Imgproc.approxPolyDP(contour2f, approxCurve, Imgproc.arcLength(contour2f, true) * 0.02, true)
            val points = MatOfPoint(*approxCurve.toArray())

            val boundRect = Imgproc.boundingRect(points)

            // Detect if it's a rectangle (notebook) or circle (can)
            val aspectRatio = boundRect.width.toDouble() / boundRect.height.toDouble()
            if (aspectRatio >= 0.8 && aspectRatio <= 1.2 && points.total() >= 8) {
                // Circle detected (like a can)
                Imgproc.circle(mRgba, Point(boundRect.x + boundRect.width / 2.0, boundRect.y + boundRect.height / 2.0),
                    Math.min(boundRect.width, boundRect.height) / 2, Scalar(0.0, 255.0, 0.0), 2)
            } else if (points.total() == 4L) {
                // Rectangle detected (like a notebook)
                Imgproc.rectangle(mRgba, Point(boundRect.x.toDouble(), boundRect.y.toDouble()),
                    Point(boundRect.x + boundRect.width.toDouble(), boundRect.y + boundRect.height.toDouble()),
                    Scalar(255.0, 0.0, 0.0), 2)
            }
        }
        Log.e("CameraFrame", "onCameraFrame")
        return mRgba
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::cameraView.isInitialized) {
            cameraView.disableView()
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED) {
            cameraView.enableView()
        }
    }

    override fun onPause() {
        super.onPause()
        cameraView.disableView()
    }
}