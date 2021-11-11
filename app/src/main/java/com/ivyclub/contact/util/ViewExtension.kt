package com.ivyclub.contact.util

import android.app.AlertDialog
import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isNotEmpty
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.transition.*
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.ivyclub.contact.R
import kotlin.math.roundToInt

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

fun View.setRotateAnimation(from: Float, to: Float) {
    val rotate = RotateAnimation(
        from,
        to,
        Animation.RELATIVE_TO_SELF,
        0.5f,
        Animation.RELATIVE_TO_SELF,
        0.5f
    ).apply {
        duration = 200
        interpolator = LinearInterpolator()
        fillAfter = true
    }
    this.startAnimation(rotate)
}

fun ViewDataBinding.hideKeyboard() {
    ViewCompat.getWindowInsetsController(this.root)?.hide(WindowInsetsCompat.Type.ime())
}

fun ViewDataBinding.showKeyboard() {
    ViewCompat.getWindowInsetsController(this.root)?.show(WindowInsetsCompat.Type.ime())
}

fun ChipGroup.setFriendChips(friendList: List<String>, chipCount: Int = friendList.size) {
    if (isNotEmpty()) removeAllViews()

    friendList.subList(0, chipCount.coerceAtMost(friendList.size)).forEachIndexed { index, name ->
        Chip(context).apply {
            text =
                if (friendList.size > chipCount && index == chipCount - 1) {
                    String.format(
                        context.getString(R.string.format_friend_count_etc),
                        name,
                        friendList.size - chipCount
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

fun ViewGroup.addChips(names: List<String>, onCloseIconClick: (Int) -> (Unit)) {
    if (childCount > 0) { removeAllViews() }

    val layoutParams = ViewGroup.MarginLayoutParams(
        ViewGroup.MarginLayoutParams.WRAP_CONTENT,
        ViewGroup.MarginLayoutParams.WRAP_CONTENT
    ).apply {
        rightMargin = context.dpToPx(4)
        topMargin = context.dpToPx(4)
    }

    names.forEachIndexed { index, name ->
        addView(
            Chip(context).apply {
                text = name
                setChipBackgroundColorResource(R.color.blue_100)
                setEnsureMinTouchTargetSize(false)
                chipMinHeight = 8f

                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    onCloseIconClick(index)
                }
            }, layoutParams
        )
    }
}

fun View.setCustomBackgroundColor(@ColorRes color: Int) {
    this.setBackgroundColor(ContextCompat.getColor(this.context, color))
}

fun Context.showAlertDialog(
   message: String,
   positiveCallback: () -> (Unit),
   negativeCallback: (() -> (Unit))? = null
) {
    AlertDialog.Builder(this)
        .setMessage(message)
        .setPositiveButton(R.string.yes) { _, _ ->
            positiveCallback.invoke()
        }
        .setNegativeButton(R.string.no) { _, _ ->
            negativeCallback?.invoke()
        }
        .show()
}

fun Context.dpToPx(dp: Int) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)
        .roundToInt()