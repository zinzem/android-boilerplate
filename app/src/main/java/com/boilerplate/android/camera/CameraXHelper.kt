package com.boilerplate.android.camera

import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.boilerplate.android.core.extentions.getDeviceRotation
import com.boilerplate.android.core.views.activities.BaseActivity
import com.orhanobut.logger.Logger

fun BaseActivity.openRearCamera(previewView: PreviewView): ImageCapture {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
    val preview = Preview.Builder()
        .build()
        .also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }
    val imageCapture = ImageCapture.Builder()
        //.setTargetRotation(getDeviceRotation())
        .build()

    cameraProviderFuture.addListener({
        // Used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)

        } catch (exception: Exception) {
            Logger.e("Use case binding failed: $exception")
        }

    }, ContextCompat.getMainExecutor(this))

    return imageCapture
}