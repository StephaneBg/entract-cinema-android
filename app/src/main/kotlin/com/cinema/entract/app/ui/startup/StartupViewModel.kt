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

package com.cinema.entract.app.ui.startup

import com.cinema.entract.core.ui.BaseViewModel
import com.cinema.entract.data.interactor.CinemaUseCase
import timber.log.Timber

class StartupViewModel(private val useCase: CinemaUseCase) :
    BaseViewModel<Nothing, StartupState>() {

    fun prefetch() = launchAsync(
        {
            state.postValue(StartupState(isLoading = true))
            useCase.getMovies()
            useCase.getDateRange()
            val eventUrl = useCase.getEventUrl()
            state.postValue(StartupState(isIdle = true, eventUrl = eventUrl))
        },
        {
            Timber.e(it)
            state.postValue(StartupState(isLoadError = true))
        }
    )
}

data class StartupState(
    val isIdle: Boolean = false,
    val isLoading: Boolean = false,
    val isLoadError: Boolean = false,
    val eventUrl: String? = null
)
