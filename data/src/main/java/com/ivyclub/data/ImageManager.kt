package com.ivyclub.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object ImageManager {
    fun saveProfileImage(currentBitmap: Bitmap?, friendId: Long) {
        if(currentBitmap == null) return
        val tempFile = File(IMAGE_FILE_PATH,"$friendId.jpg")
        try {
            tempFile.createNewFile()
            val out = FileOutputStream(tempFile)
            currentBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            out.close()
        } catch (e: Exception) {

        }
    }

    fun loadProfileImage(friendId: Long): Bitmap? {
        return try {
            BitmapFactory.decodeFile("$IMAGE_FILE_PATH$friendId.jpg")
        } catch(e: OutOfMemoryError) {
            null
        }
    }


    private const val IMAGE_FILE_PATH = "/data/user/0/com.ivyclub.contact/cache/"

}