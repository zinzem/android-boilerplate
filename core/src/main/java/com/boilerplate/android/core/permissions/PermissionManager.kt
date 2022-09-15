package com.boilerplate.android.core.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.boilerplate.android.core.R
import com.boilerplate.android.core.extentions.fromHtmlCompat
import com.boilerplate.android.core.repositories.SharedPreferencesRepository
import com.google.android.material.snackbar.Snackbar

private const val NOT_GRANTED = -1

interface PermissionManager {

    fun isPermissionGranted(context: Context, permission: String): Boolean
    fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        permissionRationalId: Int,
        requestCode: Int,
        critical: Boolean = false,
        callback: (granted: Boolean) -> Unit = {}
    )
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
}

class PermissionManagerImpl(
        private val sharedPreferencesStore: SharedPreferencesRepository
) : PermissionManager {

    private val queue = mutableMapOf<Int, MutableList<(Boolean) -> Unit>>()

    override fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun requestPermissions(
        activity: Activity,
        permissions: Array<String>,
        @StringRes permissionRationalId: Int,
        requestCode: Int,
        critical: Boolean,
        callback: (granted: Boolean) -> Unit
    ) {
        var showRational = false
        var didAskPermission = false
        val rational = fromHtmlCompat(
            "<font color=\"#ffffff\">${activity.getString(permissionRationalId)}</font>"
        )

        queue[requestCode]?.add(callback)

        permissions.forEach {
            if (!showRational) showRational = ActivityCompat.shouldShowRequestPermissionRationale(activity, it)
            if (!didAskPermission) didAskPermission = sharedPreferencesStore.didAskPermission(it)
        }

        if (showRational) {
            Snackbar.make(activity.findViewById(android.R.id.content), rational, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { ActivityCompat.requestPermissions(activity, permissions, requestCode) }
                    .setActionTextColor(activity.resources.getColor(R.color.white))
                    .show()
        } else if (didAskPermission && critical) {
            Snackbar.make(activity.findViewById(android.R.id.content), rational, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) { goToAppDetailActivity(activity) }
                    .setActionTextColor(activity.resources.getColor(R.color.white))
                    .show()
        } else {
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
            permissions.forEach { sharedPreferencesStore.permissionAsked(it) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val allGranted = !grantResults.contains(NOT_GRANTED)
        queue[requestCode]?.forEach { it.invoke(allGranted) }
        queue.remove(requestCode)
    }

    private fun goToAppDetailActivity(activity: Activity) {
        val intent = Intent()
        val uri = Uri.fromParts("package", activity.packageName, null)
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = uri
        activity.startActivity(intent)
    }
}