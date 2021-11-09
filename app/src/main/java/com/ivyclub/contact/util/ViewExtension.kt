package com.ivyclub.contact.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.transition.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ivyclub.contact.R

fun <T : ViewDataBinding> ViewGroup.binding(
    @LayoutRes layoutRes: Int,
    attachToParent: Boolean = false
): T {
    return DataBindingUtil.inflate(LayoutInflater.from(context), layoutRes, this, attachToParent)
}

fun View.changeVisibilityWithDirection(
    direction: Int,
    visibility: Int,
    animationTime: Long,
    callback: () -> Unit = {}
) {
    val transition: Transition = TransitionSet().apply {
        addTransition(Fade())
        addTransition(Slide(direction))
        duration = animationTime
        addTarget(this@changeVisibilityWithDirection)
        addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {}
            override fun onTransitionEnd(transition: Transition) {
                (this@changeVisibilityWithDirection).visibility = visibility
                callback.invoke()
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


fun ViewDataBinding.showKeyboard() {
    ViewCompat.getWindowInsetsController(this.root)?.show(WindowInsetsCompat.Type.ime())
}

fun ChipGroup.setFriendChips(friendList: List<String>, actualCount: Int) {
    if (isNotEmpty()) removeAllViews()

    friendList.forEachIndexed { index, name ->
        Chip(context).apply {
            text =
                if (actualCount > 3 && index == 2) {
                    String.format(
                        context.getString(R.string.format_friend_count_etc),
                        name,
                        actualCount - 3
                    )
                } else {
                    name
                }
            isEnabled = false
            setChipBackgroundColorResource(R.color.blue_100)
            setEnsureMinTouchTargetSize(false)
            chipMinHeight = 8f
        }.also {
            addView(it)
        }
    }
}