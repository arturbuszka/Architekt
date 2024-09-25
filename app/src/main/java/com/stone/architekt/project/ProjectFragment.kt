package com.stone.architekt.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.stone.architekt.databinding.FragmentProjectBinding


class ProjectFragment : Fragment() {
    private lateinit var viewModel: ProjectViewModel
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentProjectBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[ProjectViewModel::class.java]
        binding.viewModel = viewModel


        viewModel.eventGallerySelected.observe(viewLifecycleOwner) { gallerySelected ->
            if (gallerySelected) {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo))
                viewModel.onGallerySelectedFinished()
            }
        }




        return binding.root

    }
}