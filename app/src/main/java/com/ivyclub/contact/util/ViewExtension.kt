package com.ivyclub.contact.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.transition.*

fun <T : ViewDataBinding> ViewGroup.binding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, attachToParent)
}

fun View.changeVisibilityWithDirection(direction: Int, visibility: Int, animationTime: Long) {
    val transition: Transition = TransitionSet().apply {
        addTransition(Fade())
        addTransition(Slide(direction))
        duration = animationTime
        addTarget(this@changeVisibilityWithDirection)
        addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                (this@changeVisibilityWithDirection).visibility = visibility
            }

            override fun onTransitionCancel(transition: Transition) {}
            override fun onTransitionPause(transition: Transition) {}
            override fun onTransitionResume(transition: Transition) {}
        })
    }
    TransitionManager.beginDelayedTransition(
        this.parent as ViewGroup,
        transition
    )
}

fun ViewDataBinding.hideKeyboard() {
    ViewCompat.getWindowInsetsController(this.root)?.hide(WindowInsetsCompat.Type.ime())
}