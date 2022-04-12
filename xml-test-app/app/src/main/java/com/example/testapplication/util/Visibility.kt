package com.example.testapplication.util

import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout

// Hides view if its parent is motionLayout
fun View.setMotionVisibility(visibility: Int) {
    val motionLayout = parent as MotionLayout
    motionLayout.constraintSetIds.forEach {
        val constraintSet = motionLayout.getConstraintSet(it) ?: return@forEach
        constraintSet.setVisibility(this.id, visibility)
        constraintSet.applyTo(motionLayout)
    }
}