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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.stone.architekt.databinding.FragmentObjectdetectorBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat

class ObjectDetectorFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {
    private lateinit var viewModel: ObjectDetectorViewModel
    private lateinit var binding: FragmentObjectdetectorBinding
    private lateinit var cameraView: CameraBridgeViewBase

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.d("Camera", "Permission granted")
            cameraView.enableView()
        } else {
            Log.e("Camera", "Permission denied")
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
        initCamera()
        requestCameraPermission()

//        viewModel.eventGallerySelected.observe(viewLifecycleOwner, Observer { notifed ->
//        })
        return binding.root
    }

    private fun initCamera() {
        cameraView = binding.cameraView
        cameraView.visibility = View.VISIBLE
        cameraView.setCameraIndex(CameraCharacteristics.LENS_FACING_FRONT)

        cameraView.setCvCameraViewListener(this)
        Log.d("Camera", "set CvCameraViewListener")
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
                Log.d("Camera", "Camera view enabled")
            }

            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                // You can show some rationale to the user
                Log.d("Camera", "Camera permission is required")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }

            else -> {
                // Request the permission
                Log.d("Camera", "Camera permission request")
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        Log.d("Camera", "Camera view started")
    }

    override fun onCameraViewStopped() {
        Log.d("Camera", "Camera view stopped")
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        Log.d("Camera", "onCameraFrame")
        return viewModel.onCameraFrame(inputFrame!!.rgba())
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