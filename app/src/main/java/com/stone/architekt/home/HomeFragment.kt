package com.stone.architekt.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stone.architekt.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentHomeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.btnNew.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionNewProject())
        }
        binding.btnObjectDetector.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGoToObjectDetector())
        }
        binding.btn3dVisualization.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionGoToDesignVisualization())
        }
        return binding.root

    }
}