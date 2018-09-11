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

package com.cinema.entract.ui.view.today

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cinema.entract.data.interactor.CinemaUseCase
import com.cinema.entract.ui.base.BaseViewModel
import com.cinema.entract.ui.base.Error
import com.cinema.entract.ui.base.Loading
import com.cinema.entract.ui.base.Resource
import com.cinema.entract.ui.base.Success
import com.cinema.entract.ui.mapper.MovieMapper
import com.cinema.entract.ui.model.Movie
import timber.log.Timber
import java.util.*

class TodayViewModel(
    useCase: CinemaUseCase,
    private val mapper: MovieMapper
) : BaseViewModel<CinemaUseCase>(useCase) {

    private val movies = MutableLiveData<Resource<List<Movie>>>()

    fun getMovies(): LiveData<Resource<List<Movie>>> {
        movies.value ?: run {
            movies.postValue(Loading())
            retrieveMovies()
        }
        return movies
    }

    fun getMovies(day: Date) = retrieveMovies(day)

    private fun retrieveMovies() = retrieveMovies(null)

    private fun retrieveMovies(day: Date?) {
        launchAsyncTryCatch(
            {
                val fetchedMovies = useCase.getMovies(day).map { mapper.mapToUi(it) }
                movies.postValue(Success(fetchedMovies))
            },
            {
                Timber.e(it)
                movies.postValue(Error(it))
            }
        )
    }
}