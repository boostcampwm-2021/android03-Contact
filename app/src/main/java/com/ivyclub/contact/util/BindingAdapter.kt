package com.ivyclub.contact.util

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
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
            .signature(ObjectKey(targetFile.lastModified()))
            .into(imageView)
    } else {
        Glide.with(imageView)
            .load(R.drawable.photo)
            .into(imageView)
    }
    imageView.clipToOutline = true
}

@BindingAdapter("bindPlanImage")
fun bindPlanImage(
    imageView: ImageView,
    imageString: String
) {
    println(imageString)
    Glide.with(imageView)
        .load(imageString)
        .into(imageView)
    imageView.clipToOutline = true
}