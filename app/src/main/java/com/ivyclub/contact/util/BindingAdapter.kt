package com.ivyclub.contact.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.ivyclub.contact.R
import java.io.File

@BindingAdapter("bindImage")
fun bindImage(
    imageView: ImageView,
    profileId: Long
) {
    val imageString = "${imageView.context.cacheDir}/$profileId.jpg"
    val targetFile = File(imageString)
    if (targetFile.exists()) {
        Glide.with(imageView)
            .load(imageString)
            .into(imageView)
    } else {
        Glide.with(imageView)
            .load(R.drawable.photo)
            .into(imageView)
    }
    imageView.clipToOutline = true
}