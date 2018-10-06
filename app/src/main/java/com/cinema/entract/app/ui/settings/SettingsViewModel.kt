package com.cinema.entract.app.ui.settings

import androidx.lifecycle.ViewModel
import com.cinema.entract.data.interactor.PreferencesUseCase

class SettingsViewModel(private val prefsUseCase: PreferencesUseCase) : ViewModel() {

    fun isEventEnabled(): Boolean = prefsUseCase.isEventEnabled()

    fun setEventPreference(enabled: Boolean) = prefsUseCase.setEventPreference(enabled)

    fun canDisplayMedia(): Boolean = prefsUseCase.canDisplayMedia()

    fun isOnlyOnWifi(): Boolean = prefsUseCase.isOnlyOnWifi()

    fun setOnlyOnWifi(onlyOnWifi: Boolean) = prefsUseCase.setOnlyOnWifi(onlyOnWifi)
}