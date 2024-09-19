package com.stone.architekt.objectdetector

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.Manifest
import android.hardware.camera2.CameraCharacteristics
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.button.MaterialButton
import com.stone.architekt.databinding.FragmentObjectdetectorBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat

class ObjectDetectorFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {
    private lateinit var viewModel: ObjectDetectorViewModel
    private lateinit var binding: FragmentObjectdetectorBinding
    private lateinit var cameraView: CameraBridgeViewBase
    private lateinit var imageView: ImageView
    private lateinit var captureButton: MaterialButton
    private lateinit var resetButton: MaterialButton
    private lateinit var loadingText: TextView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("objectdetector", "Permission granted")
            cameraView.enableView()
        } else {
            Log.e("objectdetector", "Permission denied")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentObjectdetectorBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(ObjectDetectorViewModel::class.java)
        binding.viewModel = viewModel
        viewModel.init()
        imageView = binding.imageView
        captureButton = binding.btnNewPhoto
        resetButton = binding.btnReset
        loadingText = binding.textLoading
        initCamera()
        requestCameraPermission()

        viewModel.cameraState.observe(viewLifecycleOwner, Observer { cameraState ->
            when (cameraState) {
                ObjectDetectorViewModel.CameraState.PREVIEWING -> showCameraPreview()
                ObjectDetectorViewModel.CameraState.CAPTURED -> showCapturedImage()
                ObjectDetectorViewModel.CameraState.PROCESSING -> showProcessing()
                ObjectDetectorViewModel.CameraState.LOADING -> showLoading()
            }
        })

        viewModel.photo.observe(viewLifecycleOwner, Observer { bitmap ->
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap)
            }
        })
        return binding.root
    }

    private fun showLoading() {
        loadingText.visibility = View.VISIBLE
        cameraView.disableView()
        cameraView.visibility = View.GONE
        imageView.visibility = View.GONE
        captureButton.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    private fun showProcessing() {
        loadingText.visibility = View.VISIBLE
        cameraView.disableView()
        cameraView.visibility = View.GONE
        imageView.visibility = View.GONE
        captureButton.visibility = View.GONE
        resetButton.visibility = View.GONE
    }

    private fun showCapturedImage() {
        loadingText.visibility = View.GONE
        cameraView.disableView()
        cameraView.visibility = View.GONE
        imageView.visibility = View.VISIBLE
        captureButton.visibility = View.GONE
        resetButton.visibility = View.VISIBLE
    }

    private fun showCameraPreview() {
        loadingText.visibility = View.GONE
        cameraView.enableView()
        cameraView.visibility = View.VISIBLE
        imageView.visibility = View.GONE
        captureButton.visibility = View.VISIBLE
        resetButton.visibility = View.GONE
    }

    private fun initCamera() {
        cameraView = binding.cameraView
        cameraView.enableView()
        cameraView.setCameraIndex(CameraCharacteristics.LENS_FACING_FRONT)
        cameraView.setCvCameraViewListener(this)
        Log.d("objectdetector", "set CvCameraViewListener")
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
                Log.d("objectdetector", "Camera view enabled")
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // You can show some rationale to the user
                Log.d("objectdetector", "Camera permission is required")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                // Request the permission
                Log.d("objectdetector", "Camera permission request")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.d("objectdetector", "Camera view started")
    }

    override fun onCameraViewStopped() {
        Log.d("objectdetector", "Camera view stopped")
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        Log.d("frame", "onCameraFrame")
        var rgba = viewModel.proccesCaputredFrame(inputFrame!!.rgba())
        return rgba
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