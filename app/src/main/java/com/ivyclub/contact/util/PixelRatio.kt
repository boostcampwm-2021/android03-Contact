package com.ivyclub.contact.util

import android.app.Application
import androidx.annotation.Px
import com.ivyclub.contact.MainApplication
import kotlin.math.roundToInt

class PixelRatio(private val app: Application) {
    private val displayMetrics
        get() = app.resources.displayMetrics

    @Px
    fun dpToPixel(dp: Int) = (dp * displayMetrics.density).roundToInt()
    fun dpToPixelFloat(dp: Float) = (dp * displayMetrics.density).roundToInt()
}

val Number.dpToPixel: Int
    get() = MainApplication.pixelRatio.dpToPixel(this.toInt())

val Number.dpToPixelFloat: Float
    get() = MainApplication.pixelRatio.dpToPixelFloat(this.toFloat()).toFloat()