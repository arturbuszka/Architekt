package com.stone.architekt.pipelineControl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.stone.architekt.databinding.DialogAddPipelineStepBinding
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.DetailEnhanceStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.MorphologicalStep
import com.stone.architekt.imageprocessing.steps.PerspectiveTransformStep


class AddStepDialogFragment(
    private val onStepSelected: (ImageProcessingStep) -> Unit
) : DialogFragment() {
    private lateinit var binding: DialogAddPipelineStepBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogAddPipelineStepBinding.inflate(inflater, container, false)

        setupRecyclerView(binding.stepsRecyclerView)

        binding.closeButton.setOnClickListener {
            dismiss()
        }
        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        val availableSteps = listOf(
            BlurStep(),
            DetailEnhanceStep(),
            EdgeDetectionStep(),
            GrayscaleStep(),
            MorphologicalStep(),
//            PerspectiveTransformStep()
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = StepAdapter(availableSteps) { step ->
            onStepSelected(step)
            dismiss()
        }
    }

}