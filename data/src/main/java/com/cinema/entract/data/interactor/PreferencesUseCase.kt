package com.cinema.entract.data.interactor

import com.cinema.entract.core.utils.NetworkUtils
import com.cinema.entract.data.repository.CinemaRepository

class PreferencesUseCase(
    private val repo: CinemaRepository,
    private val networkUtils: NetworkUtils
) {

    fun isEventEnabled(): Boolean = repo.getUserPreferences().isEventEnabled()

    fun setEventPreference(enabled: Boolean) = repo.getUserPreferences().setEventPreference(enabled)

    fun canDisplayMedia(): Boolean =
        repo.getUserPreferences().isDataEnabled() || networkUtils.isConnectedOnWifi()

    fun isDataEnabled(): Boolean = repo.getUserPreferences().isDataEnabled()

    fun setDataPreference(enabled: Boolean) = repo.getUserPreferences().setDataPreference(enabled)
}