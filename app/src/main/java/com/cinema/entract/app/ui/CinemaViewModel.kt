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

package com.cinema.entract.app.ui

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
import com.cinema.entract.data.interactor.CinemaUseCase
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber

class CinemaViewModel(
    useCase: CinemaUseCase,
    private val mapper: MovieMapper
) : BaseViewModel<CinemaUseCase>(useCase) {

    private val movies = MutableLiveData<Resource<List<Movie>>>()
    private val date = MutableLiveData<String>()
    private val selectedMovie = MutableLiveData<Resource<Movie>>()

    fun getMovies(): LiveData<Resource<List<Movie>>> {
        movies.value ?: retrieveMovies()
        return movies
    }

    fun getDate(): LiveData<String> {
        date.value ?: updateDate()
        return date
    }

    fun getMovies(day: LocalDate) {
        retrieveMovies(day)
        updateDate(day)
    }

    fun getDateRange(): DateRange? = useCase.dateRange?.let {
        DateRange(LocalDate.parse(it.minimumDate), LocalDate.parse(it.maximumDate))
    }

    fun getSelectedMovie(): LiveData<Resource<Movie>> = selectedMovie

    fun setSelectedMovie(movie: Movie) = selectedMovie.postValue(Success(movie))

    fun retrieveMovies(day: LocalDate? = null) {
        movies.postValue(Loading())
        launchAsyncTryCatch(
            {
                val date = (day ?: LocalDate.now()).format()
                val fetchedMovies = useCase.getMovies(date).map { mapper.mapToUi(it) }
                movies.postValue(Success(fetchedMovies))
            },
            {
                Timber.e(it)
                movies.postValue(Error(it))
                date.postValue("Entract Cinema")
            }
        )
    }

    private fun updateDate(day: LocalDate? = null) {
        val formattedDate = (day ?: LocalDate.now()).format()
        date.postValue(formattedDate)
    }

    private fun LocalDate.format() = this.format(DateTimeFormatter.ISO_LOCAL_DATE)
}