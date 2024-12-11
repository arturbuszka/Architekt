package com.stone.architekt.pipelineControl

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.stone.architekt.R
import com.stone.architekt.imageprocessing.pipeline.ImageProcessingStep
import com.stone.architekt.imageprocessing.steps.BlurStep
import com.stone.architekt.imageprocessing.steps.DetailEnhanceStep
import com.stone.architekt.imageprocessing.steps.EdgeDetectionStep
import com.stone.architekt.imageprocessing.steps.GrayscaleStep
import com.stone.architekt.imageprocessing.steps.MorphologicalStep
import com.stone.architekt.imageprocessing.steps.PerspectiveTransformStep

class StepAdapter(
    private val steps: List<ImageProcessingStep>,
    private val onStepClick: (ImageProcessingStep) -> Unit
) : RecyclerView.Adapter<StepAdapter.StepViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StepViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pipeline_step, parent, false)

        return StepViewHolder(view, onStepClick)

    }

    override fun onBindViewHolder(holder: StepViewHolder, position: Int) {
        holder.bind(steps[position])
    }

    override fun getItemCount(): Int = steps.size


    class StepViewHolder(
        itemView: View,
        private val onStepClick: (ImageProcessingStep) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val stepName: TextView = itemView.findViewById(R.id.step_name)
        private val stepIcon: ImageView = itemView.findViewById(R.id.step_icon)


        fun bind(step: ImageProcessingStep) {
            stepName.text = step.javaClass.name
            stepIcon.setImageResource(getIconForStep(step))
            itemView.setOnClickListener {
                onStepClick(step)
            }

        }

        private fun getIconForStep(step: ImageProcessingStep): Int {
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
}