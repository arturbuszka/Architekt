package com.stone.architekt.objectdetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import org.opencv.core.Mat

class CapturedFrameViewModel : ViewModel() {
    private val _matRepository = InMemoryMatRepository
    private var _frame = MutableLiveData<Mat?>()
    val frame: LiveData<Mat?>
        get() = _frame

    val customMode = CustomImageProcessingPipelineMode()

    init {
    }

    fun initFrame() {
        val capturedFrame = _matRepository.getMat("captured_frame")
        if (capturedFrame != null) {
            setFrame(capturedFrame)
        } else {
            Log.d("CapturedFrameViewModel", "captured frame mat is empty")
        }
    }

    private fun setFrame(frame: Mat?) {
        _frame.value = frame
    }

    fun reprocessFrame() {
        val currentFrame = _frame.value
        if (currentFrame != null && !currentFrame.empty()) {
            val processedStep = customMode.processCapturedFrame(currentFrame)
            setFrame(processedStep)
        }
    }

    fun addStep(step: ImageProcessingStep) {
        customMode.addStep(step)
    }

    fun removeStep(step: ImageProcessingStep) {
        customMode.removeStep(step)
    }


}