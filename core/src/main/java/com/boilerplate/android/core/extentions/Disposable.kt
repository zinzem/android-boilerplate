package com.boilerplate.android.core.extentions

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.boilerplate.android.core.views.viewmodels.BaseVM
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.autoDisposeWith(lifecycleOwner: LifecycleOwner) {
    val autoDisposable = AutoDisposable(this)

    lifecycleOwner.lifecycle.addObserver(autoDisposable)
}

fun Disposable.autoDisposeWith(baseViewModel: BaseVM) = addTo(baseViewModel.disposables)

fun Disposable.addTo(disposeBag: CompositeDisposable) = disposeBag.add(this)

private class AutoDisposable(var disposable: Disposable? = null): LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun dispose() {
        disposable?.apply { if (!isDisposed) dispose() }
    }
}