package com.stone.architekt.project

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProjectViewModel : ViewModel() {
    private val _eventGallerySelected = MutableLiveData<Boolean>()
    val eventGallerySelected: LiveData<Boolean>
        get() = _eventGallerySelected

    fun onGallerySelect() {
        _eventGallerySelected.value = true
    }

    fun onGallerySelectedFinished() {
        _eventGallerySelected.value = false
    }


}