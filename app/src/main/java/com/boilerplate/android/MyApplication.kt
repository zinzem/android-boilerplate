package com.boilerplate.android

import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import androidx.multidex.MultiDexApplication
import com.boilerplate.android.core.di.coreModule
import com.boilerplate.android.di.appModule
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.MESSAGE
import org.koin.core.module.Module

class MyApplication: MultiDexApplication(),  CameraXConfig.Provider {

    private val modules : List<Module> = listOf(appModule, coreModule)

    override fun onCreate() {
        super.onCreate()

        // Init Logger
        Logger.addLogAdapter(AndroidLogAdapter())

        // Init DI
        startKoin {
            androidContext(this@MyApplication)

            logger(object: org.koin.core.logger.Logger() {
                override fun log(level: Level, msg: MESSAGE) {
                    when(level) {
                        Level.DEBUG -> Logger.d("koin: $msg")
                        Level.INFO -> Logger.i("koin: $msg")
                        Level.ERROR -> Logger.e("koin: $msg")
                    }
                }
            })

            modules(modules)
        }
    }

    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}