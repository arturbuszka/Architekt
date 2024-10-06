package com.stone.architekt.designVisualization

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stone.architekt.databinding.FragmentDesignvisualizationBinding
import com.stone.architekt.designVisualization.scene.SceneRenderer

class DesignVisualizationFragment : Fragment() {
    private lateinit var binding: FragmentDesignvisualizationBinding
    private lateinit var viewModel: DesignVisualizationViewModel
    private lateinit var glSurfaceView: GLSurfaceView
    private lateinit var renderer: SceneRenderer

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDesignvisualizationBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[DesignVisualizationViewModel::class.java]
        binding.viewModel = viewModel
        glSurfaceView = binding.sceneView
        // Initialize OpenGL ES 2.0
        glSurfaceView.setEGLContextClientVersion(2)
        renderer = SceneRenderer()
        glSurfaceView.setRenderer(renderer)

        // Set the render mode to continuously update the scene
        glSurfaceView.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        glSurfaceView.onResume()
    }

    override fun onPause() {
        super.onPause()
        glSurfaceView.onPause()
    }


}