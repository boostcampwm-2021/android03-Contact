package com.ivyclub.contact.util

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun Activity.uriToBitmap(uri: Uri): Bitmap = if (Build.VERSION.SDK_INT < 28) {
    val bitmap = MediaStore.Images.Media.getBitmap(
        this.contentResolver,
        uri
    )
    bitmap
} else {
    val source =
        ImageDecoder.createSource(this.contentResolver, uri)
    val bitmap = ImageDecoder.decodeBitmap(source)
    bitmap
}