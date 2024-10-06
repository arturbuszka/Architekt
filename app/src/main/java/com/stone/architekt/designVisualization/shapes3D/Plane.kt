package com.stone.architekt.designVisualization.shapes3D

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Plane {
    private val vertexData = floatArrayOf(
        -5f, 0f, -5f,  // Bottom-left
        -5f, 0f, 5f,   // Top-left
        5f, 0f, -5f,   // Bottom-right
        5f, 0f, 5f     // Top-right
    )

    private var vertexBuffer: FloatBuffer = createBuffer(vertexData)

    fun draw(vpMatrix: FloatArray) {
        // Similar to the cube, draw the flat plane with vertex buffer
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, vertexData.size / 3)
    }

    private fun createBuffer(data: FloatArray): FloatBuffer {
        val buffer = ByteBuffer.allocateDirect(data.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        buffer.put(data).position(0)
        return buffer
    }
}