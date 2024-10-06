package com.stone.architekt.designVisualization.shapes3D

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class Cube {
    private val vertexData = floatArrayOf(
        // Front face
        -1f,  1f,  1f,   // top-left
        -1f, -1f,  1f,   // bottom-left
        1f, -1f,  1f,   // bottom-right
        1f,  1f,  1f,   // top-right

        // Back face
        -1f,  1f, -1f,   // top-left
        -1f, -1f, -1f,   // bottom-left
        1f, -1f, -1f,   // bottom-right
        1f,  1f, -1f,   // top-right

        // Left face
        -1f,  1f, -1f,   // top-left
        -1f, -1f, -1f,   // bottom-left
        -1f, -1f,  1f,   // bottom-right
        -1f,  1f,  1f,   // top-right

        // Right face
        1f,  1f, -1f,   // top-left
        1f, -1f, -1f,   // bottom-left
        1f, -1f,  1f,   // bottom-right
        1f,  1f,  1f,   // top-right

        // Top face
        -1f,  1f, -1f,   // top-left
        -1f,  1f,  1f,   // bottom-left
        1f,  1f,  1f,   // bottom-right
        1f,  1f, -1f,   // top-right

        // Bottom face
        -1f, -1f, -1f,   // top-left
        -1f, -1f,  1f,   // bottom-left
        1f, -1f,  1f,   // bottom-right
        1f, -1f, -1f    // top-right
    )
    private val colorData = floatArrayOf(
        // Front face (Red)
        1f, 0f, 0f, 1f,   // top-left
        1f, 0f, 0f, 1f,   // bottom-left
        1f, 0f, 0f, 1f,   // bottom-right
        1f, 0f, 0f, 1f,   // top-right

        // Back face (Green)
        0f, 1f, 0f, 1f,   // top-left
        0f, 1f, 0f, 1f,   // bottom-left
        0f, 1f, 0f, 1f,   // bottom-right
        0f, 1f, 0f, 1f,   // top-right

        // Left face (Blue)
        0f, 0f, 1f, 1f,   // top-left
        0f, 0f, 1f, 1f,   // bottom-left
        0f, 0f, 1f, 1f,   // bottom-right
        0f, 0f, 1f, 1f,   // top-right

        // Right face (Yellow)
        1f, 1f, 0f, 1f,   // top-left
        1f, 1f, 0f, 1f,   // bottom-left
        1f, 1f, 0f, 1f,   // bottom-right
        1f, 1f, 0f, 1f,   // top-right

        // Top face (Cyan)
        0f, 1f, 1f, 1f,   // top-left
        0f, 1f, 1f, 1f,   // bottom-left
        0f, 1f, 1f, 1f,   // bottom-right
        0f, 1f, 1f, 1f,   // top-right

        // Bottom face (Magenta)
        1f, 0f, 1f, 1f,   // top-left
        1f, 0f, 1f, 1f,   // bottom-left
        1f, 0f, 1f, 1f,   // bottom-right
        1f, 0f, 1f, 1f    // top-right
    )

    private var vertexBuffer: FloatBuffer = createBuffer(vertexData)
    private var colorBuffer: FloatBuffer = createBuffer(colorData)

    init {

    }

    fun draw(vpMatrix: FloatArray) {
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