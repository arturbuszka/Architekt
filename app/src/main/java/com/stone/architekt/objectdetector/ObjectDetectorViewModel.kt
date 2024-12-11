package com.stone.architekt.objectdetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stone.architekt.imageprocessing.camera.CameraMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.opencv.core.Mat

class ObjectDetectorViewModel : ViewModel() {

    enum class DetectionMode {
        LIVE_DETECTION,
        DEFAULT
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


    private lateinit var _cameraMode: CameraMode
    private lateinit var _currentFrame: Mat

    init {
        _currentFrame = Mat()
        setCameraState(CameraState.READY)
        setMode(DetectionMode.DEFAULT)
    }

    private fun setCameraState(state: CameraState) {
        _cameraState.value = state
    }

    private fun setMode(mode: DetectionMode) {
        _currentMode.value = mode
        _cameraMode = when (mode) {
            DetectionMode.LIVE_DETECTION -> LiveDetectionMode()
            DetectionMode.DEFAULT -> DefaultMode()
        }
    }


    fun switchToLiveDetection() {
        setMode(DetectionMode.LIVE_DETECTION)
    }

    fun switchToScanDocument() {
        setMode(DetectionMode.DEFAULT)
    }


    fun resetCamera() {
        setCameraState(CameraState.READY)
        _currentFrame = Mat()
    }

    fun proccesCaputredFrame(frame: Mat): Mat {
        _currentFrame = _cameraMode.processCapturedFrame(frame)
        return _currentFrame
    }

    fun onCaptureFrame() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    setCameraState(CameraState.LOADING)
                }
                Log.d("objectdetector", "Starting heavy processing")
                val mat = _currentFrame.clone();
                if (mat.empty()) {
                    Log.d("objectdetector", "on capture frame mat is empty")
                }
                InMemoryMatRepository.saveMat("captured_frame", mat)

                Log.d("objectdetector", "Heavy processing complete")

                withContext(Dispatchers.Main) {
                    Log.d("objectdetector", "Switching to Main Thread")
                    setCameraState(CameraState.CAPTURED)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Log.d("objectdetector", "Error during processing")
                    setCameraState(CameraState.READY)
                }
            }
        }
    }
}