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

    // 기존 파일 삭제하고 새로운 bitmap list로 다시 파일들 생성
    fun savePlanBitmap(bitmapList: List<Bitmap>, planId: String) {
        runCatching {
            val folderPath = "${ImageType.PLAN_IMAGE.filePath}${planId}/"
            val file = File(folderPath)
            if (file.exists()) file.deleteRecursively()
            file.mkdirs()
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

    fun deletePlanImageFolder(planId: String) {
        runCatching {
            val folderPath = "${ImageType.PLAN_IMAGE.filePath}${planId}/"
            val folder = File(folderPath)
            if (folder.exists()) {
                folder.listFiles()?.forEach {
                    it.delete()
                }
                folder.delete()
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

    fun deleteImage(friendId: Long) {
        val tempFile = File(ImageType.PROFILE_IMAGE.name, "$friendId.jpg")
        tempFile.delete()
    }
}