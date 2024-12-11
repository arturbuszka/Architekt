package com.stone.architekt.pipelineControl

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.stone.architekt.R
import com.stone.architekt.databinding.DialogPipelineControlBinding
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import com.stone.architekt.objectdetector.CustomImageProcessingPipelineMode


class PipelineControlDialogFragment(
    private val customMode: CustomImageProcessingPipelineMode,
    private val onUpdate: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogPipelineControlBinding
    private lateinit var viewModel: PipelineControlDialogViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogPipelineControlBinding.inflate(inflater)
        binding.lifecycleOwner = this
        viewModel = ViewModelProvider(this)[PipelineControlDialogViewModel::class.java]
        binding.viewModel = viewModel

        setupStepsContainer()

        binding.plusIcon.setOnClickListener {
            showAddStepDialog()
        }

//        setupStepsContainer()
//        setupObservers()
//
//        binding.closeButton.setOnClickListener {
//            binding.closeButton.setColorFilter(
//                ContextCompat.getColor(
//                    requireContext(),
//                    R.color.icon_active
//                )
//            )
//            dismiss()
//        }

        return binding.root
    }

    private fun showAddStepDialog() {
        val dialog = AddStepDialogFragment { selectedStepCalss ->
//            val step = selectedStepCalss.ne
        }
    }


    private fun setupStepsContainer() {
        binding.stepsContainer.removeAllViews()
        for (step in customMode.getSteps()) {
            val stepIcon = ImageView(requireContext())
            stepIcon.setImageResource(viewModel.getIconForStep(step))
            stepIcon.layoutParams = LinearLayout.LayoutParams(48.dp, 48.dp).apply {
                marginEnd = 8.dp
            }
            stepIcon.setOnClickListener {
//                showStepParametersDialog(step)
            }
            binding.stepsContainer.addView(stepIcon)
        }
    }


//    private fun setupObservers() {
//        viewModel.selectedStep.observe(viewLifecycleOwner) { step ->
//            if (step != null) {
//                showStepParameters(step)
//            }
//        }
//    }
//
//    private fun setupStepsContainer() {
//        binding.stepsContainer.removeAllViews()
//        for (step in viewModel.getPipelineSteps()) {
//            val stepIcon = ImageView(requireContext())
//            stepIcon.setImageResource(viewModel.getIconForStep(step))
//            stepIcon.layoutParams = LinearLayout.LayoutParams(48.dp, 48.dp).apply {
//                marginEnd = 8.dp
//            }
//            stepIcon.setOnClickListener {
//                viewModel.onStepSelected(step)
//                showStepParameters(step)
//            }
//            binding.stepsContainer.addView(stepIcon)
//        }
//
//        val plusIcon = ImageView(requireContext()).apply {
//            setImageResource(R.drawable.ic_plus)
//            layoutParams = LinearLayout.LayoutParams(48.dp, 48.dp)
//            setOnClickListener { showAddStepDialog() }
//        }
//        binding.stepsContainer.addView(plusIcon)
//    }
//
//    private fun showStepParameters(step: ImageProcessingStep) {
//        binding.stepsParamaetersContainer.visibility = View.VISIBLE
//        binding.parameterTitle.text = step.javaClass.simpleName
//
////        binding.parameterSeekbar.progress = (step.getPara)
//        binding.parameterValue.text = binding.parameterSeekbar.progress.toString()
//
//        binding.parameterSeekbar.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
////                step.setParameterValue(progress.toDouble())
//                binding.parameterValue.text = progress.toString()
//                onUpdate()
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
//        })
//
//
//    }
//
//    private fun showAddStepDialog() {
//        val addStepDialog = AddStepDialogFragment { selectedStep ->
//            viewModel.addStepToPipeline(selectedStep)
//            onUpdate()
//            setupStepsContainer()
//        }
//    }

    val Int.dp: Int
        get() = (this * Resources.getSystem().displayMetrics.density).toInt()


}