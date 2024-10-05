package com.stone.architekt.objectdetector

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stone.architekt.imageprocessing.camera.CameraMode
import org.opencv.android.OpenCVLoader
import org.opencv.core.Mat

class ObjectDetectorViewModel : ViewModel() {

    enum class DetectionMode {
        LIVE_DETECTION,
        SCAN_DOCUMENT
    }

    enum class CameraState {
        READY,
        CAPTURED,
        ERROR,
        LOADING
    }

    private val _currentMode = MutableLiveData<DetectionMode>()
    val currentMode: LiveData<DetectionMode> = _currentMode

    private val _cameraState = MutableLiveData<CameraState>()
    val cameraState: LiveData<CameraState>
        get() = _cameraState

    private var _newPhoto = MutableLiveData<Bitmap?>()
    val photo: LiveData<Bitmap?>
        get() = _newPhoto

    private lateinit var cameraMode: CameraMode

    init {
        loadDependencies()
        setCameraState(CameraState.READY)
        setMode(DetectionMode.LIVE_DETECTION)
    }

    private fun setCameraState(state: CameraState) {
        _cameraState.value = state
    }

    private fun setPhoto(new: Bitmap?) {
        _newPhoto.value = new
    }

    private fun setMode(mode: DetectionMode) {
        _currentMode.value = mode
        cameraMode = when (mode) {
            DetectionMode.LIVE_DETECTION -> LiveDetectionMode()
            DetectionMode.SCAN_DOCUMENT -> ScanDocumentMode()
        }
    }


    fun switchToLiveDetection() {
        setMode(DetectionMode.LIVE_DETECTION)
    }

    fun switchToScanDocument() {
        setMode(DetectionMode.SCAN_DOCUMENT)
    }


    fun resetCamera() {
        setCameraState(CameraState.READY)
        setPhoto(null)
    }

    fun proccesCaputredFrame(frame: Mat): Mat {
        return cameraMode.processCapturedFrame(frame)
    }

    fun onCaptureFrame() {
//        viewModelScope.launch(Dispatchers.IO) {
//            try {
//                withContext(Dispatchers.Main) {
//                    setCameraState(CameraState.LOADING)
//                }
//                Log.d("objectdetector", "Starting heavy processing")
//                val bitmap = convertMatToBitmap(boundingBoxFrameCaptured.clone())
//                Log.d("objectdetector", "Heavy processing complete")
//
//                withContext(Dispatchers.Main) {
//                    Log.d("objectdetector", "Switching to Main Thread")
//                    setPhoto(bitmap)
//                    setCameraState(CameraState.CAPTURED)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                withContext(Dispatchers.Main) {
//                    Log.d("objectdetector", "Error during processing")
//                    setCameraState(CameraState.READY)
//                }
//            }
//        }
    }






    private fun loadDependencies() {
        if (!OpenCVLoader.initLocal()) {
            Log.e("OpenCV", "Unable to load OpenCV")
        } else {
            Log.d("OpenCV", "OpenCV loaded successfully")
        }
    }
}