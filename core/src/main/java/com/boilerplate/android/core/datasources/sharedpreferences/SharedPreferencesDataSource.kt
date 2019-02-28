package  com.boilerplate.android.core.datasources.sharedpreferences

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesDataSource(context: Context) {

    private val default: SharedPreferences
    private val defaultEditor: SharedPreferences.Editor
        get() = default.edit()

    init {
        val defaultPreferenceName = context.packageName + "_preferences"
        default = context.getSharedPreferences(defaultPreferenceName, Context.MODE_PRIVATE)
    }

    operator fun set(`object`: Any, key: String) {
        when (`object`) {
            is Boolean -> defaultEditor.putBoolean(key, `object`).apply()
            is String -> defaultEditor.putString(key, `object`).apply()
            is Int -> defaultEditor.putInt(key, `object`).apply()
            is Long -> defaultEditor.putLong(key, `object`).apply()
            is Float -> defaultEditor.putFloat(key, `object`).apply()
        }
    }

    fun getString(key: String): String? {
        return default.getString(key, null)
    }

    fun getLong(key: String): Long {
        return default.getLong(key, 0)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        return default.getLong(key, defaultValue)
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return default.getBoolean(key, defaultValue)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return default.getInt(key, defaultValue)
    }

    fun containsKey(key: String): Boolean {
        return default.contains(key)
    }

    fun remove(key: String) {
        defaultEditor.remove(key).apply()
    }
}
