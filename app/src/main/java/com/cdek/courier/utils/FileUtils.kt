package com.cdek.courier.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import android.view.View
import java.text.SimpleDateFormat
import java.util.*


object FileUtils {
    fun resizeAndSaveFile(photo: File): String {
        var result = ""
        val fullBitmap = BitmapFactory.decodeFile(photo.absolutePath)
        val resizedBitmap = Bitmap.createScaledBitmap(
            fullBitmap,
            (fullBitmap.width * INDEX_SIZE).toInt(),
            (fullBitmap.height * INDEX_SIZE).toInt(), true
        )
        var os: OutputStream? = null
        try {
            os = FileOutputStream(photo)
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, INDEX_QUALITY, os)
            os.flush()
        } catch (e: IOException) {
            result = e.toString()
        } finally {
            try {
                os?.close()
            } catch (e: Exception) {
                result += e.toString()
            }
        }
        return result
    }

    fun newPhotoFile(filePath: File): File {
        val fileName = SimpleDateFormat(
            "ddMMyyyy_HHmmss",
            Locale.getDefault()
        ).format(Date()) + ".jpeg"
        return File(filePath.absolutePath, fileName)
    }

    fun initBitmapFromView(fromView: View): Bitmap {
        fromView.layout(0, 0, fromView.measuredWidth, fromView.measuredHeight)
        val bitmap = Bitmap.createBitmap(
            fromView.measuredWidth,
            fromView.measuredHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        val background = fromView.background
        background?.draw(canvas)
        fromView.draw(canvas)
        return bitmap
    }

    fun saveBitmapToFile(bitmap: Bitmap, imagePath: String): String {
        var result = ""
        var os: OutputStream? = null
        val imageFile = File(imagePath)
        try {
            os = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, INDEX_QUALITY, os)
            os.flush()
        } catch (e: IOException) {
            result = e.toString()
        } finally {
            try {
                os?.close()
            } catch (e: Exception) {
                result += e.toString()
            }
        }
        return result
    }
}