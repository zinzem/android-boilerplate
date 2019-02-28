package com.boilerplate.android.core.repositories

import android.location.Geocoder
import android.location.Location
import com.boilerplate.android.core.datasources.location.FusedLocationDataSource
import io.reactivex.Observable
import io.reactivex.Single
import org.koin.standalone.KoinComponent

open class LocationRepository(
    fusedLocationDataSource: FusedLocationDataSource,
    private val geocoder: Geocoder
): KoinComponent {

    open val locationStream: Observable<Location> = fusedLocationDataSource.locationStream

    open fun getAddress(location: Location): Single<String> {
        return Single.fromCallable {
            geocoder.getFromLocation(location.latitude, location.longitude, 1)
        }.flatMap {
            Single.just(it[0])
        }.map { address ->
            (0..address.maxAddressLineIndex).joinToString(separator = "\n") { address.getAddressLine(it) }
        }
    }
}