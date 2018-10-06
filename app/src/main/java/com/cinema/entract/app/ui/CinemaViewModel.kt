package com.cinema.entract.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cinema.entract.core.ui.BaseViewModel
import com.cinema.entract.data.interactor.CinemaUseCase
import com.cinema.entract.data.interactor.PreferencesUseCase

class CinemaViewModel(
    private val useCase: CinemaUseCase,
    private val prefsUseCase: PreferencesUseCase
) : BaseViewModel() {

    private val eventUrl = MutableLiveData<String?>()

    fun getEventUrl(): LiveData<String?> {
        eventUrl.value ?: if (prefsUseCase.isEventEnabled()) loadEventUrl()
        return eventUrl
    }

    private fun loadEventUrl() {
        launchAsync(
            {
                val url = useCase.getEventUrl()
                eventUrl.postValue(url)
            },
            ::emptyCallback
        )
    }
}