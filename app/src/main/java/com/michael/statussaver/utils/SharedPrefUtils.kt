package com.michael.statussaver.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefUtils {
    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    fun init(context: Context) {
        preferences = context.getSharedPreferences(
            "${"Status Saver".replace(" ", "_")}_prefs",
            Context.MODE_PRIVATE
        )
    }

    fun getPrefString(key: String, defaultValue: String): String? {
        return preferences.getString(key, defaultValue)
    }

    fun getPrefBoolean(key: String, defaultValue: Boolean): Boolean {
        return preferences.getBoolean(key, defaultValue)
    }

    fun getPrefInt(key: String, defaultValue: Int): Int {
        return preferences.getInt(key, defaultValue)
    }

    fun putPrefString(key: String, value: String): Boolean {
        editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
        return true
    }

    fun putPrefBoolean(key: String, value: Boolean): Boolean {
        editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
        return true
    }

    fun putPrefInt(key: String, value: Int): Boolean {
        editor = preferences.edit()
        editor.putInt(key, value)
        editor.apply()
        return true
    }

    // To remember the download status
    fun putPrefDownloadState(fileName: String, isDownloaded: Boolean): Boolean {
        editor = preferences.edit()
        editor.putBoolean(fileName, isDownloaded)
        editor.apply()
        return true
    }
    fun getPrefDownloadState(fileName: String): Boolean {
        return preferences.getBoolean(fileName, false)
    }
}

object SharedPrefKeys {
    const val PREF_KEY_WP_PERMISSION_GRANTED = "PREF_KEY_WP_PERMISSION_GRANTED"
    const val PREF_KEY_WP_TREE_URI = "PREF_KEY_WP_TREE_URI"
    const val PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED = "PREF_KEY_WP_BUSINESS_PERMISSION_GRANTED"
    const val PREF_KEY_WP_BUSINESS_TREE_URI = "PREF_KEY_WP_BUSINESS_TREE_URI"
    const val PREF_KEY_DOWNLOADED_PERMISSION_GRANTED = "PREF_KEY_DOWNLOADED_PERMISSION_GRANTED"
    const val PREF_KEY_DOWNLOADED_TREE_URI = "PREF_KEY_DOWNLOADED_TREE_URI"

}