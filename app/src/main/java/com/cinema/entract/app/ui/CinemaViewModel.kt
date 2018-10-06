/*
 * Copyright 2018 Stéphane Baiget
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

    private val eventUrl = MutableLiveData<String>()

    fun getEventUrl(): LiveData<String> {
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