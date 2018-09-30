/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.app.ui.movies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.model.DateRange
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.base.BaseViewModel
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.State
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.interactor.CinemaUseCase
import org.threeten.bp.LocalDate
import timber.log.Timber

class MoviesViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper
) : BaseViewModel() {

    private val state = MutableLiveData<State<OnScreen>>()

    var dateRange: DateRange? = null
        private set

    fun getState(): LiveData<State<OnScreen>> {
        state.value ?: retrieveMovies()
        return state
    }

    fun retrieveMovies(date: LocalDate) {
        useCase.selectDate(date)
        retrieveMovies()
    }

    fun retrieveMovies() {
        state.postValue(Loading())
        launchAsync(
            {
                val (movies, range) = useCase.getMovies()
                state.postValue(Success(
                        movies.map { movieMapper.mapToUi(it) } to useCase.getDate().longFormatToUi()
                    ))
                dateRange = DateRange(range.minimumDate, range.maximumDate)
            },
            {
                Timber.e(it)
                state.postValue(Error(it))
            }
        )
    }
}

typealias OnScreen = Pair<List<Movie>, String>
fun OnScreen.getMovies() = this.first
fun OnScreen.getDate() = this.second