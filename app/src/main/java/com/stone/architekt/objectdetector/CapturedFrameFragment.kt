package com.stone.architekt.objectdetector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.stone.architekt.databinding.FragmentCapturedframeBinding

class CapturedFrameFragment : Fragment() {
    private lateinit var viewModel: CapturedFrameViewModel
    private lateinit var binding: FragmentCapturedframeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCapturedframeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(CapturedFrameViewModel::class.java)
        binding.viewModel = viewModel
        return binding.root
    }

}