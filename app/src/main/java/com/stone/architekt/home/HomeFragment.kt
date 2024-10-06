package com.stone.architekt.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stone.architekt.R
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
            binding.btnNew.setColorFilter(ContextCompat.getColor(requireContext(), R.color.icon_active))
            findNavController().navigate(HomeFragmentDirections.actionNewProject())
        }
        binding.btnObjectDetector.setOnClickListener {
            binding.btnObjectDetector.setColorFilter(ContextCompat.getColor(requireContext(), R.color.icon_active))
            findNavController().navigate(HomeFragmentDirections.actionGoToObjectDetector())
        }
        binding.btn3dVisualization.setOnClickListener {
            binding.btn3dVisualization.setColorFilter(ContextCompat.getColor(requireContext(), R.color.icon_active))
            findNavController().navigate(HomeFragmentDirections.actionGoToDesignVisualization())
        }
        return binding.root

    }
}