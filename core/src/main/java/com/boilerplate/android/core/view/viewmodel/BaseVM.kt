package com.boilerplate.android.core.views.viewmodels

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseVM : ViewModel() {

    val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}