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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cinema.entract.data.interactor.CinemaUseCase
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import timber.log.Timber

class StartupViewModel(private val useCase: CinemaUseCase) : ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        innerState.postValue(StartupState(isLoaded = true))
    }

    private val innerState = MutableLiveData<StartupState>()
    val state: LiveData<StartupState> = innerState

    fun prefetch() = viewModelScope.launch(exceptionHandler) {
        innerState.postValue(StartupState(isLoading = true))
        useCase.getMovies()
        useCase.getDateRange()
        innerState.postValue(
            StartupState(isIdle = true, eventUrl = useCase.getEventUrl().peekContent())
        )
    }
}

data class StartupState(
    val isIdle: Boolean = false,
    val isLoading: Boolean = false,
    val isLoaded: Boolean = false,
    val eventUrl: String? = null
)
