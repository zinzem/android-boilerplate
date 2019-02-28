package com.boilerplate.android

import androidx.multidex.MultiDexApplication
import com.boilerplate.android.core.di.coreModule
import com.boilerplate.android.di.appModule
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

class MyApplication: MultiDexApplication() {

    private val modules : List<Module> = listOf(appModule, coreModule)

    override fun onCreate() {
        super.onCreate()

        // Init Logger
        Logger.addLogAdapter(AndroidLogAdapter())

        // Init DI
        startKoin(this, modules, logger = object: org.koin.log.Logger {
            override fun info(msg: String) { Logger.i("koin: $msg") }
            override fun debug(msg: String) { Logger.d("koin: $msg") }
            override fun err(msg: String) { Logger.e("koin: $msg") }
        })
    }
}