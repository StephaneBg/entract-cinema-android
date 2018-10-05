package com.cinema.entract.cache.repository

import android.content.Context
import androidx.core.content.edit
import com.cinema.entract.data.repository.UserPreferences

class UserPreferencesImpl(context: Context) : UserPreferences {

    private val preferences = context.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE)

    override fun isEventEnabled(): Boolean = preferences.getBoolean(USER_PREF_EVENT, true)

    override fun setEventPreference(enabled: Boolean) = preferences.edit {
        putBoolean(USER_PREF_EVENT, enabled)
    }

    override fun isDataEnabled(): Boolean = preferences.getBoolean(USER_PREF_DATA, true)

    override fun setDataPreference(enabled: Boolean) = preferences.edit {
        putBoolean(USER_PREF_DATA, enabled)
    }

    companion object {
        private const val USER_PREFS = "USER_PREFS"
        private const val USER_PREF_EVENT = "USER_PREF_EVENT"
        private const val USER_PREF_DATA = "USER_PREF_DATA"
    }
}