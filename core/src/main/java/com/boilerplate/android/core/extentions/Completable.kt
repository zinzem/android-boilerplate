package com.boilerplate.android.core.extentions

import com.orhanobut.logger.Logger
import io.reactivex.Completable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

fun Completable.subscribeSafe(
        onComplete: Action = Action { },
        onError: Consumer<Throwable> = Consumer { Logger.e(it, "Completable subscribeSafe error") }
): Disposable {
    return this.subscribe(onComplete, onError)
}

fun Completable.subscribeSafe(onComplete: () -> Unit = { }): Disposable {
    return this.subscribe(onComplete, { Logger.e(it, "Completable subscribeSafe error") })
}