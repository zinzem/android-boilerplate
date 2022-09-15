package com.boilerplate.android.views.widgets

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.google.mlkit.vision.segmentation.SegmentationMask
import java.nio.ByteBuffer


class SegmentationOutput(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private val transformationMatrix = Matrix()

    var image: Bitmap? = null
    var segmentationMask: SegmentationMask? = null
    var targetWidth: Int = 0
    var targetHeight: Int = 0

    override fun onDraw(canvas: Canvas) {
        segmentationMask?.let {
            val mask = it.buffer
            val maskWidth = it.width
            val maskHeight = it.height

            mask.rewind()
            for (y in 0 until maskHeight) {
                for (x in 0 until maskWidth) {
                    val backgroundLikelihood = 1 - mask.float
                    if (backgroundLikelihood > 0.33) {
                        image!!.setPixel(x, y, Color.argb(0, 0, 0, 0))
                    }
                }
            }

            transformationMatrix.reset()
            if (targetWidth > targetHeight && image!!.width > image!!.height) {
                val scaleImageX = targetWidth * 1f / image!!.width
                val scaleImageY = targetHeight * 1f / image!!.height
                transformationMatrix.postScale(scaleImageX, scaleImageY)
                transformationMatrix.postScale(-1f, 1f, width / 2f, height / 2f)
                canvas.drawBitmap(image!!, transformationMatrix, null)
            } else {
                //val scaleImageX = targetHeight * 1f / image!!.width
                //val scaleImageY = targetWidth * 1f / image!!.height
                transformationMatrix.postScale(1.5F, 1.5F)
                transformationMatrix.postScale(-1f, 1f, width / 2f, height / 2f)
                canvas.drawBitmap(image!!, transformationMatrix, null)
            }
        }
    }

    private fun maskColorsFromByteBuffer(
        byteBuffer: ByteBuffer,
        maskWidth: Int,
        maskHeight: Int
    ): IntArray {
        @ColorInt val colors = IntArray(maskWidth * maskHeight)
        for (i in 0 until maskWidth * maskHeight) {
            val backgroundLikelihood = 1 - byteBuffer.float
            if (backgroundLikelihood > 0.9) {
                colors[i] = Color.argb(128, 255, 0, 255)
            } else if (backgroundLikelihood > 0.2) {
                // Linear interpolation to make sure when backgroundLikelihood is 0.2, the alpha is 0 and
                // when backgroundLikelihood is 0.9, the alpha is 128.
                // +0.5 to round the float value to the nearest int.
                val alpha = (182.9 * backgroundLikelihood - 36.6 + 0.5).toInt()
                colors[i] = Color.argb(alpha, 255, 0, 255)
            }
        }
        return colors
    }
}