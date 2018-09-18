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
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.interactor.CinemaUseCase
import org.threeten.bp.LocalDate
import timber.log.Timber

class MoviesViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper
) : BaseViewModel() {

    private val moviesLiveData = MutableLiveData<Resource<List<Movie>>>()
    private val dateLiveData = MutableLiveData<Resource<String>>()

    var dateRange: DateRange? = null
        private set

    fun getMovies(): LiveData<Resource<List<Movie>>> {
        moviesLiveData.value ?: retrieveMovies()
        return moviesLiveData
    }

    fun getDate(): LiveData<Resource<String>> {
        dateLiveData.value ?: updateDate()
        return dateLiveData
    }

    fun selectMovie(movie: Movie) {
        useCase.selectMovie(movie.id)
    }

    fun retrieveMovies(date: LocalDate) {
        useCase.selectDate(date)
        updateDate(date)
        retrieveMovies()
    }

    fun retrieveMovies() {
        moviesLiveData.postValue(Loading())
        launchAsync(
            {
                val (movies, range) = useCase.getMovies()
                moviesLiveData.postValue(Success(movies.map { movieMapper.mapToUi(it) }))
                dateRange = DateRange(range.minimumDate, range.maximumDate)
            },
            {
                Timber.e(it)
                moviesLiveData.postValue(Error(it))
                dateLiveData.postValue(Success(null))
            }
        )
    }

    private fun updateDate(day: LocalDate? = null) {
        val formattedDate = (day ?: LocalDate.now()).longFormatToUi()
        dateLiveData.postValue(Success(formattedDate))
    }
}