package com.ivyclub.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import kotlin.runCatching

object ImageManager {
    fun saveProfileImage(currentBitmap: Bitmap?, friendId: Long) {
        if (currentBitmap == null) return
        val tempFile = File(IMAGE_FILE_PATH, "$friendId.jpg")
        runCatching {
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            out.close()
        }.onFailure { exception ->
            Log.e("ImageManagerError", "Exception : $exception")
        }
    }

    fun loadProfileImage(friendId: Long): Bitmap? {
        return runCatching {
            BitmapFactory.decodeFile("$IMAGE_FILE_PATH$friendId.jpg")
        }.mapCatching { loadedImage ->
            loadedImage
        }.getOrDefault(null)
    }

    private const val IMAGE_FILE_PATH = "/data/user/0/com.ivyclub.contact/cache/"
}