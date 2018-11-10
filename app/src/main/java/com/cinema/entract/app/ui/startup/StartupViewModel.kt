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
import com.cinema.entract.core.ui.ScopedViewModel
import com.cinema.entract.data.interactor.CinemaUseCase
import kotlinx.coroutines.coroutineScope

class StartupViewModel(private val useCase: CinemaUseCase) : ScopedViewModel() {

    private val eventUrl = MutableLiveData<String>()

    fun prefetch(): LiveData<String> {
        eventUrl.value ?: launchAsync(::prefetchData, ::onPrefetchDataError)
        return eventUrl
    }

    private suspend fun prefetchData() = coroutineScope {
        useCase.getMovies()
        useCase.getDateRange()
        eventUrl.postValue(useCase.getEventUrl())
    }

    private fun onPrefetchDataError(throwable: Throwable) {
        eventUrl.postValue("")
    }
}