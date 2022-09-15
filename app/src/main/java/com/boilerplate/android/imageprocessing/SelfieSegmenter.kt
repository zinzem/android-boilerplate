/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.boilerplate.android.imageprocessing

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import com.boilerplate.android.googlesample.BitmapUtils
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.SegmentationMask
import com.google.mlkit.vision.segmentation.Segmenter
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import com.orhanobut.logger.Logger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class SelfieSegmenter {

  private val segmenter: Segmenter
  private val transformationMatrix = Matrix()

  init {
    val options = SelfieSegmenterOptions.Builder().apply {
      setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
      //enableRawSizeMask()
    }.build()
    segmenter = Segmentation.getClient(options)
    Logger.d("SegmenterProcessor created with option: " + options)
  }

  fun detectInImage(image: InputImage): Task<SegmentationMask> {
    return segmenter.process(image)
  }

  suspend fun getSegmentedBitmap(inputImage: InputImage, bitmapImage: Bitmap) = suspendCancellableCoroutine<Bitmap> { continuation ->
    segmenter.process(inputImage).addOnFailureListener {
      continuation.cancel(it)
    }.addOnSuccessListener {
      val mask = it.buffer
      val maskWidth = it.width
      val maskHeight = it.height

      mask.rewind()
      for (y in 0 until maskHeight) {
        for (x in 0 until maskWidth) {
          val backgroundLikelihood = 1 - mask.float
          if (backgroundLikelihood > 0.33) {
            bitmapImage.setPixel(x, y, Color.TRANSPARENT)
          }
        }
      }

      continuation.resume(bitmapImage)
    }
  }
}
