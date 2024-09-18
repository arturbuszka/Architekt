package com.stone.architekt.objectdetector

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.opencv.core.Mat

class ObjectDetectorViewModel : ViewModel() {

    private val _cameraFrame = MutableLiveData<Mat>()
    val cameraFrame: LiveData<Mat> get() = _cameraFrame

    fun updateFrame(frame:Mat) {
        _cameraFrame.value = frame
    }

    private val _eventGallerySelected = MutableLiveData<Boolean>()
    val eventGallerySelected: LiveData<Boolean>
        get() = _eventGallerySelected

    fun onGallerySelected() {
        _eventGallerySelected.value = true
    }

    fun onGallerySelectedFinished() {
        _eventGallerySelected.value = false
    }


}