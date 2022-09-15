package com.boilerplate.android.di

import com.boilerplate.android.camera.CameraHelper
import com.boilerplate.android.imageprocessing.SelfieSegmenter
import org.koin.dsl.module

val appModule = module {

    factory { CameraHelper() }
    factory { SelfieSegmenter() }
}
