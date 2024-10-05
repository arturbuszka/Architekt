package com.stone.architekt.objectdetector

import android.Manifest
import android.animation.PropertyValuesHolder
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stone.architekt.R
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
        setupObservers()
        setupUIInteractions()


        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
        }
        viewModel.resetCamera()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (::cameraView.isInitialized) {
            cameraView.disableView()
            viewModel.resetCamera()
        }
    }

    private fun initCamera() {
        cameraView = binding.cameraView
        cameraView.enableView()
        cameraView.setCameraIndex(CameraCharacteristics.LENS_FACING_FRONT)
        cameraView.setCvCameraViewListener(this)
        Log.d("objectdetector", "set CvCameraViewListener")
    }

    private fun showCameraPreview() {
        captureButton.isEnabled = true
        cameraView.enableView()
        cameraView.visibility = View.VISIBLE
        captureButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun showCapturedImage() {
//        val uri = saveBitmapToFile(viewModel.photo.value)
        captureButton.isEnabled = false
        findNavController().navigate(ObjectDetectorFragmentDirections.actionShowCapturedFrame(""))
        cameraView.disableView()
        cameraView.visibility = View.GONE
        captureButton.visibility = View.GONE
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun setupObservers() {
        viewModel.currentMode.observe(viewLifecycleOwner) { mode ->
            updateUIForMode(mode)
        }

        viewModel.cameraState.observe(viewLifecycleOwner) { cameraState ->
            when (cameraState) {
                ObjectDetectorViewModel.CameraState.READY -> showCameraPreview()
                ObjectDetectorViewModel.CameraState.CAPTURED -> showCapturedImage()
                ObjectDetectorViewModel.CameraState.ERROR -> showCameraPreview()
                ObjectDetectorViewModel.CameraState.LOADING -> showLoading()
            }
        }
    }

    private fun updateUIForMode(mode: ObjectDetectorViewModel.DetectionMode) {
        when (mode) {
            ObjectDetectorViewModel.DetectionMode.LIVE_DETECTION -> {
                binding.btnLiveDetection.setColorFilter(
                    resources.getColor(
                        R.color.icon_active,
                        null
                    )
                )
                binding.btnScanDocument.setColorFilter(
                    resources.getColor(
                        R.color.icon_inactive,
                        null
                    )
                )
            }

            ObjectDetectorViewModel.DetectionMode.SCAN_DOCUMENT -> {
                binding.btnLiveDetection.setColorFilter(
                    resources.getColor(
                        R.color.icon_inactive,
                        null
                    )
                )
                binding.btnScanDocument.setColorFilter(
                    resources.getColor(
                        R.color.icon_active,
                        null
                    )
                )
            }
        }
    }

    private fun setupUIInteractions() {
        setupCaptureButtonAnimation()
        setupLiveDetectionButtonAnimation()
        setupScanDocumentButtonAnimation()
    }

    private fun setupLiveDetectionButtonAnimation() {
        binding.btnLiveDetection.setOnClickListener {
            viewModel.switchToLiveDetection()
        }
    }

    private fun setupScanDocumentButtonAnimation() {
        binding.btnScanDocument.setOnClickListener {
            viewModel.switchToScanDocument()
        }
    }

    private fun setupCaptureButtonAnimation() {
        binding.btnNewPhoto.setOnClickListener {
            animateCaptureButton(it)
            viewModel.onCaptureFrame()
        }
    }

    private fun animateCaptureButton(view: View) {
        val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.1f, 1.0f)
        val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.1f, 1.0f)

        val animator = android.animation.ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY)
        animator.duration = 150
        animator.start()
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