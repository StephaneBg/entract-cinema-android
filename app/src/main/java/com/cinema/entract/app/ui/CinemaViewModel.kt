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
import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.model.DateRange
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.core.ui.Error
import com.cinema.entract.core.ui.Loading
import com.cinema.entract.core.ui.ScopedViewModel
import com.cinema.entract.core.ui.State
import com.cinema.entract.core.ui.Success
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.interactor.CinemaUseCase
import com.cinema.entract.data.interactor.PreferencesUseCase
import kotlinx.coroutines.coroutineScope
import org.threeten.bp.LocalDate
import timber.log.Timber

class CinemaViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper,
    private val scheduleMapper: ScheduleMapper,
    private val prefsUseCase: PreferencesUseCase
) : ScopedViewModel() {

    private val onScreenState = MutableLiveData<State<OnScreen>>()
    private val scheduleState = MutableLiveData<State<List<ScheduleEntry>>>()
    private val eventUrl = MutableLiveData<String>()

    var dateRange: DateRange? = null
        private set

    fun getOnScreenState(): LiveData<State<OnScreen>> {
        onScreenState.value ?: retrieveMovies()
        return onScreenState
    }

    fun retrieveMovies(date: LocalDate) {
        useCase.selectDate(date)
        retrieveMovies()
    }

    fun retrieveMovies() {
        onScreenState.postValue(Loading())
        launchAsync(::onSuccess, ::onError)
    }

    private suspend fun onSuccess() = coroutineScope {
        val movies = useCase.getMovies().map { movieMapper.mapToUi(it) }
        val range = useCase.getDateRange()
        dateRange = DateRange(range.minimumDate, range.maximumDate)
        onScreenState.postValue(Success(movies to useCase.getDate().longFormatToUi()))
    }

    private fun onError(throwable: Throwable) {
        Timber.e(throwable)
        onScreenState.postValue(Error(throwable))
    }

    fun getScheduleState(): LiveData<State<List<ScheduleEntry>>> {
        scheduleState.value ?: retrieveSchedule()
        return scheduleState
    }

    fun retrieveSchedule() {
        scheduleState.postValue(Loading())
        launchAsync(
            {
                val schedule = useCase.getSchedule()
                scheduleState.postValue(Success(scheduleMapper.mapToUi(schedule)))
            },
            {
                Timber.e(it)
                scheduleState.postValue(Error(it))
            }
        )
    }

    fun selectDate(date: LocalDate) {
        useCase.selectDate(date)
        retrieveMovies(date)
    }

    fun getEventUrl(): LiveData<String> {
        eventUrl.value ?: if (prefsUseCase.isEventEnabled()) loadEventUrl()
        return eventUrl
    }

    private fun loadEventUrl() {
        launchAsync(
            {
                eventUrl.postValue(useCase.getEventUrl())
            },
            ::emptyCallback
        )
    }
}

typealias OnScreen = Pair<List<Movie>, String>

fun OnScreen.getMovies() = this.first
fun OnScreen.getDate() = this.second