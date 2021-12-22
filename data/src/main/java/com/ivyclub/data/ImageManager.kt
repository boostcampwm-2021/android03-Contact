package com.ivyclub.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object ImageManager {
    fun saveProfileImage(currentBitmap: Bitmap?, friendId: Long) {
        if (currentBitmap == null) return
        runCatching {
            val tempFile = File(IMAGE_FILE_PATH, "$friendId.jpg")
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
        }.onFailure { exception ->
            Log.e("LoadProfileImageFailure", ": $exception")
        }.getOrDefault(null)
    }

//    fun loadImageWithURI(uri: Uri): Bitmap {
//        return runCatching {
//
//        }
//    }

    fun deleteImage(friendId: Long) {
        val tempFile = File(IMAGE_FILE_PATH, "$friendId.jpg")
        tempFile.delete()
    }

    private const val IMAGE_FILE_PATH = "/data/user/0/com.ivyclub.contact/cache/"
}