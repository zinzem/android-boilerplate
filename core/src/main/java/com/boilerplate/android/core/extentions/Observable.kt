package com.boilerplate.android.core.extentions

import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

fun <T> Observable<T>.subscribeSafe(
    onNext: (t: T) -> Unit,
    onError: (t: Throwable) -> Unit
): Disposable {
    return this.subscribe(onNext, onError)
}

fun <T> Observable<T>.subscribeSafe(onNext: (t: T) -> Unit = {}): Disposable {
    return this.subscribe(onNext, { Logger.e(it, "Observable subscribeSafe error") })
}