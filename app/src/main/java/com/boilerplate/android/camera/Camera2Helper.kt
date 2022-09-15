package com.boilerplate.android.camera

import android.annotation.SuppressLint
import android.hardware.camera2.*
import android.hardware.camera2.CameraCharacteristics.*
import android.hardware.camera2.CameraMetadata.REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.Surface
import androidx.camera.core.impl.utils.CompareSizesByArea
import com.orhanobut.logger.Logger
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class CameraHelper {

    var frontHandler: Handler? = null

    private var frontCamera: CameraDevice? = null
    private var frontSession: CameraCaptureSession? = null
    private var frontThread: HandlerThread? = null

    @SuppressLint("MissingPermission")
    suspend fun openFrontCamera(
        manager: CameraManager,
    ) : CameraDevice = suspendCancellableCoroutine {
        val cameraId = manager.cameraIdList.first {
            val characteristics = manager.getCameraCharacteristics(it)
            val capabilities = characteristics.get(REQUEST_AVAILABLE_CAPABILITIES)
            val cameraDirection = characteristics.get(LENS_FACING)
            capabilities?.contains(REQUEST_AVAILABLE_CAPABILITIES_BACKWARD_COMPATIBLE) == true
                    && cameraDirection == LENS_FACING_FRONT
        }

        startBackgroundThread()
        manager.openCamera(cameraId, object : CameraDevice.StateCallback() {

            override fun onOpened(device: CameraDevice) {
                frontCamera = device
                it.resume(device)
            }

            override fun onDisconnected(device: CameraDevice) {
                Logger.e("Camera $cameraId has been disconnected")
            }

            override fun onError(device: CameraDevice, error: Int) {
                val msg = when (error) {
                    ERROR_CAMERA_DEVICE -> "Fatal (device)"
                    ERROR_CAMERA_DISABLED -> "Device policy"
                    ERROR_CAMERA_IN_USE -> "Camera in use"
                    ERROR_CAMERA_SERVICE -> "Fatal (service)"
                    ERROR_MAX_CAMERAS_IN_USE -> "Maximum cameras in use"
                    else -> "Unknown"
                }
                val exception = RuntimeException("Camera $cameraId error: ($error) $msg")
                it.cancel(exception)
                Logger.e(exception.message ?: "")
            }
        }, frontHandler)
    }

    fun createFrontCaptureSession(
        targets: List<Surface>
    ) {
        frontCamera?.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {

            override fun onConfigured(session: CameraCaptureSession) {
                frontSession = session
            }

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exception = RuntimeException("Camera ${session.device.id} session configuration failed")
                Logger.e(exception.message ?: "")
            }
        }, frontHandler)
    }

    fun createCaptureRequest(templateType: Int) = frontCamera?.createCaptureRequest(templateType)

    fun setRepeatingRequest(
        captureRequest: CaptureRequest
    ) = frontSession?.setRepeatingRequest(captureRequest, null, frontHandler)

    fun capture(
        captureRequest: CaptureRequest
    ) = frontSession?.capture(captureRequest, null, frontHandler)

    fun closeFrontCamera() {
        stopBackgroundThread()
        frontSession?.stopRepeating()
        frontSession?.abortCaptures()
        frontSession?.close()
        frontCamera?.close()
    }

    private fun startBackgroundThread() {
        frontThread = HandlerThread("CameraBackgroundFront").apply { start() }
        frontHandler = Handler(frontThread!!.looper)
    }

    private fun stopBackgroundThread() {
        frontThread?.quitSafely()
        try {
            frontThread?.join()
            frontThread = null
            frontHandler = null
        } catch (e: InterruptedException) {
            Logger.e(e.toString())
        }
    }






    private val ORIENTATIONS = SparseIntArray()

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    fun getRotationCompensation(
        deviceRotation: Int,
        cameraRotation: Int,
        isFrontFacing: Boolean
    ): Int {
        val rotationCompensation = ORIENTATIONS.get(deviceRotation)

         return if (isFrontFacing) {
            (cameraRotation + rotationCompensation) % 360
        } else {
            (cameraRotation - rotationCompensation + 360) % 360
        }
    }
}

fun CameraManager.getHighestResolutionAvailable(cameraId: String, imageFormat: Int): Size{
        val sizes = getCameraCharacteristics(cameraId)
            .get(SCALER_STREAM_CONFIGURATION_MAP)!!
            .getOutputSizes(imageFormat)

        return sizes.maxByOrNull { it.height * it.width } ?: sizes.first()
    }

fun CameraManager.getLowestResolutionAvailable(cameraId: String, imageFormat: Int): Size  {
    val sizes = getCameraCharacteristics(cameraId)
        .get(SCALER_STREAM_CONFIGURATION_MAP)!!
        .getOutputSizes(imageFormat)

    return sizes.minByOrNull { it.height * it.width } ?: sizes.first()
}

@SuppressLint("RestrictedApi")
fun CameraManager.getClosestResolutionTo(cameraId: String, width: Int, height: Int, imageFormat: Int): Size {
    val sizes = getCameraCharacteristics(cameraId)
        .get(SCALER_STREAM_CONFIGURATION_MAP)!!
        .getOutputSizes(imageFormat)
    val bigEnough: MutableList<Size> = ArrayList()
    val notBigEnough: MutableList<Size> = ArrayList()
    for (option in sizes) {
        if (option.width >= width && option.height >= height) {
            bigEnough.add(option)
        } else {
            notBigEnough.add(option)
        }
    }

    return when {
        bigEnough.size > 0 -> Collections.min(bigEnough, CompareSizesByArea())
        notBigEnough.size > 0 -> Collections.max(notBigEnough, CompareSizesByArea())
        else -> sizes[0]
    }
}
fun CameraManager.getSensorOrientation(cameraId: String) =
    getCameraCharacteristics(cameraId)
        .get(SENSOR_ORIENTATION) ?: 0
