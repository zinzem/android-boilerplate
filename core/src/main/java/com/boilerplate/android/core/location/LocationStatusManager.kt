package com.boilerplate.android.core.location

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.orhanobut.logger.Logger

class LocationStatusManager {

    private val queue = mutableMapOf<Int, (Boolean) -> Unit>()

    open fun isGpsEnabled(activity: Activity): Boolean {
        val manager = activity.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return manager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)
    }

    fun enableGPS(activity: Activity, resolutionRequestCode: Int, callback: (Boolean) -> Unit) {
        val request = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
        val task = LocationServices.getSettingsClient(activity).checkLocationSettings(builder.build())

        task.addOnCompleteListener {
            try {
                task.getResult(ApiException::class.java)
                callback(true)
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(activity, resolutionRequestCode)
                            queue[resolutionRequestCode] = callback
                        } catch (e: Exception) {
                            callback(false)
                            Logger.e(e, "Could not resolve Location Setting error")
                        }
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        callback(false)
                        Logger.e("Location Setting change unavailable")
                    }
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val success = resultCode == RESULT_OK
        queue[requestCode]?.apply {
            invoke(success)
            queue.remove(requestCode)
        }
    }
}