package com.stone.architekt.pipelineControl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.stone.architekt.R
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import com.stone.architekt.imageprocessing.pipeline.ProcessingPipeline
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.DetailEnhanceStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.MorphologicalStep
import com.stone.architekt.imageprocessing.steps.PerspectiveTransformStep

class PipelineControlDialogViewModel : ViewModel() {

//    private val pipeline = ProcessingPipeline()
//    private val _selectedStep = MutableLiveData<ImageProcessingStep?>()
//    val selectedStep: LiveData<ImageProcessingStep?> = _selectedStep
//
//    fun getPipelineSteps(): List<ImageProcessingStep> {
//        return pipeline.getSteps()
//    }
//
//    fun addStepToPipeline(step: ImageProcessingStep) {
//        pipeline.addStep(step)
//    }
//
//    fun onStepSelected(step: ImageProcessingStep) {
//        _selectedStep.value = step
//    }

    fun getIconForStep(step: ImageProcessingStep): Int {
        return when (step) {
            is BlurStep -> R.drawable.ic_blur
            is DetailEnhanceStep -> R.drawable.ic_detail_enhance
            is EdgeDetectionStep -> R.drawable.ic_edge_detection
            is GrayscaleStep -> R.drawable.ic_grayscale
            is MorphologicalStep -> R.drawable.ic_morph
            is PerspectiveTransformStep -> R.drawable.ic_perspective
            else -> R.drawable.ic_unknown
        }
    }

}