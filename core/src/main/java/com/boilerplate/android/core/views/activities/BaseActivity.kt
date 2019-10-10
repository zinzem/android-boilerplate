package com.boilerplate.android.core.views.activities

import androidx.appcompat.app.AppCompatActivity
import com.boilerplate.android.core.permissions.PermissionManager
import org.koin.android.ext.android.inject

open class BaseActivity: AppCompatActivity() {

    private val permissionManager: PermissionManager by inject()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}