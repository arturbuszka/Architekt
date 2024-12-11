package com.stone.architekt.objectdetector

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.stone.architekt.R
import com.stone.architekt.databinding.FragmentCapturedframeBinding
import com.stone.architekt.pipelineControl.PipelineControlDialogFragment

class CapturedFrameFragment : Fragment() {

    private lateinit var viewModel: CapturedFrameViewModel
    private lateinit var binding: FragmentCapturedframeBinding
    private lateinit var imageView: ImageView

    private lateinit var resetButton: ImageButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCapturedframeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[CapturedFrameViewModel::class.java]
        binding.viewModel = viewModel

        imageView = binding.capturedFrame
        resetButton = binding.btnReset

        viewModel.initFrame()

        setupObservers()

        resetButton.setOnClickListener {
            resetButton.setColorFilter(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.icon_active
                )
            )
            findNavController().popBackStack()
//            resetButton.setColorFilter(ContextCompat.getColor(requireContext(), R.color.white))
        }

        return binding.root
    }

    private fun setupObservers() {
        viewModel.frame.observe(viewLifecycleOwner) { frame ->
            if (frame != null && !frame.empty()) {
                imageView.setImageBitmap(convertMatToBitmap(frame))
            }
        }
    }

    private fun showPipelineControlDialog() {
//        val dialog = PipelineControlDialogFragment(viewModel.customMode) {
//            viewModel.reprocessFrame()
//        }
//        dialog.show(parentFragmentManager, "PipelineControlDialog")
    }

//    private fun loadImageFromUri(uri: Uri) {
//        val bitmap =
//            BitmapFactory.decodeStream(requireContext().contentResolver.openInputStream(uri))
//        imageView.setImageBitmap(bitmap)
//    }

}