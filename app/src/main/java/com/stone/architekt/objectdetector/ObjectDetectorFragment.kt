package com.stone.architekt.objectdetector

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.Manifest
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.view.animation.AlphaAnimation
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stone.architekt.databinding.FragmentObjectdetectorBinding
import org.opencv.android.CameraBridgeViewBase
import org.opencv.core.Mat
import java.io.File
import java.io.FileOutputStream

class ObjectDetectorFragment : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {
    private lateinit var viewModel: ObjectDetectorViewModel

    private lateinit var binding: FragmentObjectdetectorBinding
    private lateinit var cameraView: CameraBridgeViewBase
    private lateinit var captureButton: View


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
    ): View {

        binding = FragmentObjectdetectorBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[ObjectDetectorViewModel::class.java]
        binding.viewModel = viewModel
        captureButton = binding.btnNewPhoto
        initCamera()
        requestCameraPermission()


        viewModel.cameraState.observe(viewLifecycleOwner) { cameraState ->
            when (cameraState) {
                ObjectDetectorViewModel.CameraState.PREVIEWING -> showCameraPreview()
                ObjectDetectorViewModel.CameraState.CAPTURED -> showCapturedImage()
                ObjectDetectorViewModel.CameraState.LOADING -> showLoading()
                null -> showCameraPreview()
            }
        }
        return binding.root
    }

    private fun playLoadingAnimation() {
        val loadingAnimation = AlphaAnimation(0.1f, 1f).apply {
            duration = 1
            repeatCount = AlphaAnimation.INFINITE
            repeatMode = AlphaAnimation.REVERSE
        }
        captureButton.startAnimation(loadingAnimation)
    }

    private fun resetLoadingAnimation() {
        captureButton.clearAnimation()
    }

    private fun showLoading() {
        captureButton.isEnabled = false
        playLoadingAnimation()
    }

    private fun showCapturedImage() {
        val uri = saveBitmapToFile(viewModel.photo.value)
        captureButton.isEnabled = false
        findNavController().navigate(ObjectDetectorFragmentDirections.actionShowCapturedFrame(uri.toString()))
        cameraView.disableView()
        cameraView.visibility = View.GONE
        captureButton.visibility = View.GONE

    }

    private fun showCameraPreview() {
        captureButton.isEnabled = true
        resetLoadingAnimation()
        cameraView.enableView()
        cameraView.visibility = View.VISIBLE
        captureButton.visibility = View.VISIBLE
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
                cameraView.setCameraPermissionGranted()
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
        val rgba = viewModel.proccesCaputredFrame(inputFrame!!.rgba())
        return rgba
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::cameraView.isInitialized) {
            cameraView.disableView()
            viewModel.resetCamera()
        }
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.resetCamera()
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap?): Uri {
        val file = File(requireContext().cacheDir, "captured_frame.png")
        val outputStream = FileOutputStream(file)
        try {
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        } finally {
            outputStream.flush()
            outputStream.close()
        }
        return Uri.fromFile(file)
    }
}