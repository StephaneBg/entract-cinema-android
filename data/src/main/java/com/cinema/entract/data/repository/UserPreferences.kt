package com.cinema.entract.data.repository

interface UserPreferences {

    fun isEventEnabled(): Boolean
    fun setEventPreference(enabled: Boolean)

    fun isDataEnabled(): Boolean
    fun setDataPreference(enabled: Boolean)
}