package com.stone.architekt.designVisualization.scene

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.stone.architekt.designVisualization.shapes2D.Square2
import com.stone.architekt.designVisualization.shapes2D.Triangle
import com.stone.architekt.designVisualization.shapes3D.Cube
import com.stone.architekt.designVisualization.shapes3D.Plane
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SceneRenderer : GLSurfaceView.Renderer {

//    private lateinit var cube: Cube
//    private lateinit var plane: Plane

    private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square2

    private var viewMatrix = FloatArray(16)
    private var projectionMatrix = FloatArray(16)
    private var vPMatrix = FloatArray(16)
//    private var cameraPosition = floatArrayOf(0f, 1f, 5f)
//    private var cameraLookAt = floatArrayOf(0f, 0f, 0f)


    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)
        GLES20.glEnable(GLES20.GL_DEPTH_TEST)

//        cube = Cube()
//        plane = Plane()
//
//        Matrix.setLookAtM(
//            viewMatrix, 0,
//            cameraPosition[0], cameraPosition[1], cameraPosition[2],
//            cameraLookAt[0], cameraLookAt[1], cameraLookAt[2],
//            0f, 1f, 0f
//        )
        // initialize a triangle
        mTriangle = Triangle()
        // initialize a square
        mSquare = Square2()
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
//        val aspectRatio: Float = width.toFloat() / height.toFloat()
//        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 3f, 10f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)

        // Calculate the projection and view transformation
        Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

        // Draw shape
        mTriangle.draw(vPMatrix)
//        Matrix.multiplyMM(vpMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
//
//        plane.draw(vpMatrix)
//        cube.draw(vpMatrix)
    }

}