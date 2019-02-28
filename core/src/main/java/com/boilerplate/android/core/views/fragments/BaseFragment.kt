package com.boilerplate.android.core.views.fragments

import com.boilerplate.android.core.views.activities.BaseActivity

open class BaseFragment: androidx.fragment.app.Fragment() {

    fun runGPSEnabledOperation(operationId: Int, operation: () -> Unit) {
        (activity as? BaseActivity)?.apply { runGPSEnabledOperation(id, operation) } ?: throw IllegalAccessError("Host should extend BaseActivity")
    }
}