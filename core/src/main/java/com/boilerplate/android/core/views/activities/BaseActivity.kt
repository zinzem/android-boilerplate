package com.boilerplate.android.core.views.activities

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import com.boilerplate.android.core.R
import com.boilerplate.android.core.location.LocationStatusManager
import com.boilerplate.android.core.permissions.PermissionManager
import org.koin.android.ext.android.inject

private const val CODE_FINE_LOCATION = 256

open class BaseActivity: AppCompatActivity() {

    private val permissionManager: PermissionManager by inject()
    private val locationStatusManager: LocationStatusManager by inject()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    fun runGPSEnabledOperation(operationId: Int, operation: () -> Unit) {
        if (!permissionManager.isPermissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionManager.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    R.string.permission_rational_fine_location,
                    operationId,
                    true) { if (it) { runGPSEnabledOperation(operationId, operation) }
            }
            return
        }

        if (!locationStatusManager.isGpsEnabled(this)) {
            locationStatusManager.enableGPS(this, operationId) { runGPSEnabledOperation(operationId, operation)}
            return
        }

        operation()
    }
}