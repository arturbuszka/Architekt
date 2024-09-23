package com.stone.architekt.objectdetector

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.stone.architekt.databinding.FragmentCapturedframeBinding

class CapturedFrameFragment : Fragment() {

    private lateinit var viewModel: CapturedFrameViewModel
    private lateinit var binding: FragmentCapturedframeBinding
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCapturedframeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProviders.of(this).get(CapturedFrameViewModel::class.java)
        binding.viewModel = viewModel
        imageView = binding.capturedFrame

        val args by navArgs<CapturedFrameFragmentArgs>()
        loadImageFromUri(Uri.parse((args.imageUri)))

        return binding.root
    }

    private fun loadImageFromUri(uri: Uri) {
        val bitmap =
            BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
        imageView.setImageBitmap(bitmap)
    }

}