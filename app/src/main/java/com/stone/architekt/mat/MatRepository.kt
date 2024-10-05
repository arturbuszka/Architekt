package com.stone.architekt.mat

import org.opencv.core.Mat

interface MatRepository {
    fun saveMat(id: String, mat: Mat)
    fun getMat(id: String): Mat?
    fun deleteMat(id: String)
    fun clear()
}