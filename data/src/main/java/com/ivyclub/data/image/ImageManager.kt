package com.ivyclub.data.image

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.File
import java.io.FileOutputStream

object ImageManager {
    fun saveProfileImage(currentBitmap: Bitmap?, friendId: Long) {
        if (currentBitmap == null) return
        runCatching {
            val tempFile = File(ImageType.PROFILE_IMAGE.filePath, "$friendId.jpg")
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            out.close()
        }.onFailure { exception ->
            Log.e("ImageManagerError", "Exception : $exception")
        }
    }

    fun savePlanBitmap(bitmapList: List<Bitmap>, currentTime: String) {
        runCatching {
            val folderPath = "${ImageType.PLAN_IMAGE.filePath}${currentTime}/"
            val file = File(folderPath)
            if (!file.exists()) file.mkdirs()
            bitmapList.forEachIndexed { index, bitmap ->
                val tempFile = File(folderPath, "${index}.jpg").apply {
                    createNewFile()
                }
                val out = FileOutputStream(tempFile)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
                out.close()
            }
        }.onFailure { exception ->
            Log.e("ImageManagerError", "Exception : $exception")
        }
    }

    fun loadProfileImage(friendId: Long): Bitmap? {
        return runCatching {
            BitmapFactory.decodeFile("${ImageType.PROFILE_IMAGE.filePath}$friendId.jpg")
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
        val tempFile = File(ImageType.PROFILE_IMAGE.name, "$friendId.jpg")
        tempFile.delete()
    }
}