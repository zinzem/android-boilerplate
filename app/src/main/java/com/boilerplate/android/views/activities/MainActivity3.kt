package com.boilerplate.android.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.*
import android.view.MotionEvent
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.impl.utils.executor.CameraXExecutors
import androidx.lifecycle.lifecycleScope
import com.boilerplate.android.R
import com.boilerplate.android.camera.*
import com.boilerplate.android.core.extentions.getDeviceRotation
import com.boilerplate.android.core.permissions.PermissionManager
import com.boilerplate.android.core.storage.saveImageInQ
import com.boilerplate.android.core.views.activities.BaseActivity
import com.boilerplate.android.googlesample.BitmapUtils
import com.boilerplate.android.imageprocessing.SelfieSegmenter
import com.google.android.gms.tasks.Task
import com.google.mlkit.common.MlKitException
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_main_3.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.io.File


private const val REQUEST_CAMERA_PERMISSION = 33

class MainActivity3 : BaseActivity() {

    private var frontLowResImageReader: ImageReader? = null
    private var frontHighResImageReader: ImageReader? = null
    private var frontCameraOrientation: Int = 0

    private var rearImageCapture: ImageCapture? = null

    private var frontResultBitmap: Bitmap? = null
    private var rearResultBitmap: Bitmap? = null

    private val permissionManager: PermissionManager by inject()
    private val cameraHelper: CameraHelper by inject()
    private val selfieSegmenter: SelfieSegmenter by inject()
    private var selfieSegmenterTask: Task<SegmentationMask>? = null

    private val onFrontLowResImageAvailableListener = ImageReader.OnImageAvailableListener {
        if (selfieSegmenterTask == null) {
            val latestImage = it.acquireNextImage()
            val rotation = cameraHelper.getRotationCompensation(getDeviceRotation(), frontCameraOrientation, true)
            val inputImage = InputImage.fromMediaImage(latestImage, rotation)
            val bitmap = BitmapUtils.getBitmap(latestImage, rotation)

            selfieSegmenterTask = selfieSegmenter.detectInImage(inputImage)
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        val exception = it.exception
                        if (exception is MlKitException) Logger.e("${exception.errorCode}")
                    } else {
                        runOnUiThread {
                            segmentation_output.image = bitmap
                            segmentation_output.segmentationMask = it.result
                            segmentation_output.invalidate()
                        }
                    }
                    latestImage.close()
                    selfieSegmenterTask = null
                }
        }
    }

    private val onFrontHighResImageAvailableListener = ImageReader.OnImageAvailableListener {
        val latestImage = it.acquireNextImage()
        val rotation = cameraHelper.getRotationCompensation(getDeviceRotation(), frontCameraOrientation, true)
        val inputImage = InputImage.fromMediaImage(latestImage, 180)
        val bitmapImage = BitmapUtils.getBitmap(latestImage, 180)

        lifecycleScope.launch(Dispatchers.IO) {
            selfieSegmenter.getSegmentedBitmap(inputImage, bitmapImage!!)
            rearResultBitmap?.let {
                //saveImageInQ(this@MainActivity3, bitmapImage, rotation)
                onPictureTaken(bitmapImage, it)
            } ?: run {
                frontResultBitmap = bitmapImage
            }
            latestImage.close()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_3)

        if (!permissionManager.isPermissionGranted(this, Manifest.permission.CAMERA)) {
            permissionManager.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                R.string.camera_permission_rationale,
                REQUEST_CAMERA_PERMISSION
            )
        }

        setup()
        rearImageCapture = openRearCamera(rear_output)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch(Dispatchers.Main) {
            openFrontCamera()
            showPreview()
        }
    }

    override fun onPause() {
        closeFrontCamera()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setup() {
        var dX = 0F
        var dY = 0F
        segmentation_output.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    dX = v.x - event.rawX
                    dY = v.y - event.rawY
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    segmentation_output.x = event.rawX + dX
                    segmentation_output.y =  event.rawY + dY
                    true
                }
                else -> false
            }
        }

        bt_take_picture.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) { takePicture() }
        }
    }

    private suspend fun openFrontCamera() {
        val cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        cameraHelper.openFrontCamera(cameraManager).also { frontCamera ->
            val minSize = cameraManager.getLowestResolutionAvailable(frontCamera.id, ImageFormat.YUV_420_888)
            val maxSize = cameraManager.getHighestResolutionAvailable(frontCamera.id, ImageFormat.YUV_420_888)

            frontCameraOrientation = cameraManager.getSensorOrientation(frontCamera.id)
            frontLowResImageReader = ImageReader.newInstance(minSize.width, minSize.height, ImageFormat.YUV_420_888, 1)
            frontLowResImageReader!!.setOnImageAvailableListener(onFrontLowResImageAvailableListener, cameraHelper.frontHandler)
            frontHighResImageReader = ImageReader.newInstance(maxSize.width, maxSize.height, ImageFormat.YUV_420_888, 1)
            frontHighResImageReader!!.setOnImageAvailableListener(onFrontHighResImageAvailableListener, cameraHelper.frontHandler)

            val lowResReaderSurface = frontLowResImageReader!!.surface
            val highResReaderSurface = frontHighResImageReader!!.surface
            val targets = listOf(lowResReaderSurface, highResReaderSurface)

            cameraHelper.createFrontCaptureSession(targets)
        }
    }

    private fun showPreview() {
        cameraHelper.createCaptureRequest(
            CameraDevice.TEMPLATE_PREVIEW
        )?.apply {
            addTarget(frontLowResImageReader!!.surface)
            cameraHelper.setRepeatingRequest(build())
        }
    }

    @SuppressLint("RestrictedApi")
    private fun takePicture() {
        val outputFileOptions = ImageCapture.OutputFileOptions.Builder(
            File("${cacheDir.absolutePath}/temp.jpg")
        ).build()

        frontResultBitmap?.recycle()
        frontResultBitmap = null
        rearResultBitmap?.recycle()
        rearResultBitmap = null

        cameraHelper.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE
        )?.apply {
            addTarget(frontHighResImageReader!!.surface)
            cameraHelper.capture(build())
        }
        rearImageCapture?.takePicture(
            outputFileOptions,
            CameraXExecutors.mainThreadExecutor(),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val bitmap = BitmapFactory.decodeFile(outputFileResults.savedUri!!.path)

                    frontResultBitmap?.let {
                        onPictureTaken(it, bitmap)
                    } ?: run {
                        rearResultBitmap = bitmap
                    }
                }

                override fun onError(exception: ImageCaptureException) {
                    Logger.e(exception.toString())
                }
            }
        )
    }

    private fun closeFrontCamera() {
        cameraHelper.closeFrontCamera()
        frontLowResImageReader?.close()
        frontHighResImageReader?.close()
    }

    private fun onPictureTaken(frontResultBitmap: Bitmap, rearResultBitmap: Bitmap) {
        val bmOverlay = Bitmap.createBitmap(rearResultBitmap.width, rearResultBitmap.height, rearResultBitmap.config)
        val canvas = Canvas(bmOverlay)
        val rotation = cameraHelper.getRotationCompensation(getDeviceRotation(), frontCameraOrientation, true)
        canvas.drawBitmap(rearResultBitmap, Matrix(), null)
        canvas.drawBitmap(frontResultBitmap, 0F, 0F, null)
        frontResultBitmap.recycle()
        rearResultBitmap.recycle()

        saveImageInQ(this, bmOverlay, rotation)
    }
}