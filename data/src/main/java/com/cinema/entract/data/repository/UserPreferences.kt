package com.cinema.entract.data.repository

interface UserPreferences {

    fun isEventEnabled(): Boolean
    fun setEventPreference(enabled: Boolean)

    fun isOnlyOnWifi(): Boolean
    fun setOnlyOnWifi(onlyOnWifi: Boolean)
}