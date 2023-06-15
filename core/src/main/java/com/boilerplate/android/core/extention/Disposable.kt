package com.boilerplate.android.core.extention

import com.boilerplate.android.core.views.viewmodels.BaseVM
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable.autoDisposeWith(baseViewModel: BaseVM) = addTo(baseViewModel.disposables)

fun Disposable.addTo(disposeBag: CompositeDisposable) = disposeBag.add(this)