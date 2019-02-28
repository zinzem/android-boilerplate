package com.boilerplate.android.core.extentions

import com.orhanobut.logger.Logger
import io.reactivex.Single
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer

fun <T> Single<T>.subscribeSafe(
        onComplete: Consumer<T> = Consumer { },
        onError: Consumer<Throwable> = Consumer { Logger.e(it, "Single subscribeSafe error") }
): Disposable {
    return this.subscribe(onComplete, onError)
}

fun <T> Single<T>.subscribeSafe(onComplete: (T) -> Unit): Disposable {
    return this.subscribe({ onComplete(it) }, { Logger.e(it, "Single subscribeSafe error") })
}