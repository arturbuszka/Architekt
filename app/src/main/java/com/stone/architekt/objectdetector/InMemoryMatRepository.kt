package com.stone.architekt.objectdetector

import com.stone.architekt.mat.MatRepository
import org.opencv.core.Mat
import java.util.concurrent.ConcurrentHashMap

object InMemoryMatRepository : MatRepository {
    // Thread-safe storage for Mat objects
    private val matStorage = ConcurrentHashMap<String, Mat>()

    override fun saveMat(id: String, mat: Mat) {
        matStorage[id] = mat
    }

    override fun getMat(id: String): Mat? {
        return matStorage[id]
    }

    override fun deleteMat(id: String) {
        matStorage.remove(id)?.release()  // Release Mat resources
    }

    override fun clear() {
        matStorage.values.forEach { it.release() }  // Release all Mats
        matStorage.clear()
    }

}