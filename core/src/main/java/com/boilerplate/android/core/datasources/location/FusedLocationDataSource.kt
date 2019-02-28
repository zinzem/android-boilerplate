package  com.boilerplate.android.core.datasources.location

import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.*
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

open class FusedLocationDataSource(context: Context): LocationCallback() {

    private val _locationStream = PublishSubject.create<Location>()
    open val locationStream: Observable<Location> =_locationStream
            .doOnSubscribe {
                start()
            }.doOnDispose {
                stop()
            }.share()

    private val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    private var running = false

    override fun onLocationResult(p0: LocationResult?) {
        p0?.lastLocation?.run { _locationStream.onNext( Location(this)) }
    }

    override fun onLocationAvailability(p0: LocationAvailability?) {
        Logger.e("FusedLocationProvider available: ${p0?.isLocationAvailable}")
    }

    @SuppressWarnings("MissingPermission")
    fun start() {
        if (running) {
            Logger.d("Cannot start FusedLocationDataSource, already running")
            return
        }

        val request = LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_UPDATE_MIN_INTERVAL)
                .setMaxWaitTime(LOCATION_UPDATE_MAX_INTERVAL)
        client.requestLocationUpdates(request, this, Looper.myLooper())
        running = true
    }

    fun stop() {
        if (!running) {
            Logger.d("Cannot stop FusedLocationDataSource, not running")
            return
        }

        client.removeLocationUpdates(this)
        running = false
    }

    companion object {

        private const val LOCATION_UPDATE_MIN_INTERVAL = 1000L
        private const val LOCATION_UPDATE_MAX_INTERVAL = 1000L

    }
}