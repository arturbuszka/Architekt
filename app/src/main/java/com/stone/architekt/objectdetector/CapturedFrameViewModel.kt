package com.stone.architekt.objectdetector

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.opencv.core.Mat

class CapturedFrameViewModel : ViewModel() {
    private val _matRepository = InMemoryMatRepository
    private var _frame = MutableLiveData<Mat?>()
    val frame: LiveData<Mat?>
        get() = _frame


    init {
        initFrame()
    }

    private fun initFrame() {
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

}