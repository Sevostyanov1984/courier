package com.cdek.courier.utils

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.view.View
import com.cdek.courier.App

val prefs: SharedPreferences =
    App.instance.applicationContext.getSharedPreferences(SCAN_PVZ_PREF_NAME, MODE_PRIVATE)


fun getPreferenceString(key: String): String {
    return prefs.getString(key, "") ?: ""
}

fun setPreferenceString(key: String, value: String) {
    prefs.edit()?.putString(key, value)?.apply()
}

fun removePreferenceString(key: String) {
    prefs.edit()?.remove(key)?.apply()
}

fun viewsVisibility(visibility: Int, vararg views: View) {
    for (view in views) {
        view.visibility = visibility
    }
}