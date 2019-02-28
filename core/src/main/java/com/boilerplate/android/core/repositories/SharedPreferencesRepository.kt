package com.boilerplate.android.core.repositories

import com.boilerplate.android.core.datasources.sharedpreferences.SharedPreferencesDataSource

class SharedPreferencesRepository(
        private val sharedPreferencesDataSource: SharedPreferencesDataSource
){

    // region Permissions
    fun didAskPermission(permission: String): Boolean {
        return sharedPreferencesDataSource.getBoolean("asked_$permission", false)
    }

    fun permissionAsked(permission: String) {
        sharedPreferencesDataSource.set(true, "asked_$permission")
    }
    // endregion
}
