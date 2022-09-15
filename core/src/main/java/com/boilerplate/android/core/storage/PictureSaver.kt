package com.boilerplate.android.core.storage

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.exifinterface.media.ExifInterface
import java.io.OutputStream

class PictureSaver {

}

fun saveImageInQ(context: Context, bitmap: Bitmap, orientation: Int): Uri {
    val contentResolver = context.contentResolver
    val filename = "IMG_${System.currentTimeMillis()}.jpg"
    var fos: OutputStream?
    var imageUri: Uri?
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
        put(MediaStore.Images.ImageColumns.TITLE, filename)
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
    }

    contentResolver.also { resolver ->
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        fos = imageUri?.let { resolver.openOutputStream(it) }
    }

    fos?.use {
        bitmap
            .rotatedBy(360 - orientation.toFloat())
            .compress(Bitmap.CompressFormat.JPEG, 100, it)
    }

    contentValues.clear()
    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
    contentResolver.update(imageUri!!, contentValues, null, null)

    return imageUri!!
}

private fun Bitmap.rotatedBy(angle: Float): Bitmap {
    val matrix = Matrix().apply { postRotate(angle) }
    return Bitmap.createBitmap(this, 0, 0, width, height, matrix, true)
}