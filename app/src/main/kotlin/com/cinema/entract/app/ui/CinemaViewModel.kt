/*
 * Copyright 2019 Stéphane Baiget
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

import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.model.DateParameters
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.core.ui.BaseViewModel
import com.cinema.entract.core.ui.Effect
import com.cinema.entract.core.ui.State
import com.cinema.entract.data.ext.toEpochMilliSecond
import com.cinema.entract.data.interactor.CinemaUseCase
import timber.log.Timber
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

class CinemaViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper,
    private val scheduleMapper: ScheduleMapper
) : BaseViewModel(CinemaState.Loading) {

    override suspend fun onError(error: Throwable) {
        Timber.e(error)
        setState(CinemaState.Error(error))
    }

    fun loadMovies() {
        action {
            setState(CinemaState.Loading)
            val movies = useCase.getMovies()
            val date = useCase.getDate()
            val dateRange = useCase.getDateRange()
            setState(
                CinemaState.OnScreen(
                    movies.map { movieMapper.mapToUi(it) },
                    DateParameters(date, dateRange?.minimumDate, dateRange?.maximumDate)
                )
            )
        }
    }

    fun selectDate(date: LocalDate) {
        useCase.setDate(date)
    }

    fun loadSchedule() {
        action {
            setState(CinemaState.Loading)
            val schedule = useCase.getSchedule()
            setState(CinemaState.Schedule(useCase.getDate(), scheduleMapper.mapToUi(schedule)))
        }
    }

    fun selectMovie(movie: Movie) {
        useCase.selectedMovie = movieMapper.mapToData(movie)
    }

    fun loadMovieDetails() {
        action(
            {
                setState(CinemaState.Loading)
                val retrievedMovie = useCase.getMovie()
                setState(
                    CinemaState.Details(
                        useCase.getDate(),
                        movieMapper.mapToUi(retrievedMovie)
                    )
                )
            },
            { exception ->
                CinemaState.Error(exception)
            }
        )
    }

    fun loadPromotional() {
        action {
            useCase.getEventUrl()?.let { sendEffect(PromotionalEffect(it)) }
        }
    }

    fun getSessionSchedule(movie: Movie): Pair<Long, Long> {
        val beginDateTime = ZonedDateTime.of(
            movie.date.year,
            movie.date.month.value,
            movie.date.dayOfMonth,
            movie.schedule.hour,
            movie.schedule.minute,
            0,
            0,
            ZoneId.systemDefault()
        )

        val endTime = movie.schedule.plus(movie.duration)
        val endDateTime = ZonedDateTime.of(
            movie.date.year,
            movie.date.month.value,
            movie.date.dayOfMonth,
            endTime.hour,
            endTime.minute,
            0,
            0,
            ZoneId.systemDefault()
        )

        return beginDateTime.toEpochMilliSecond() to endDateTime.toEpochMilliSecond()
    }
}

sealed class CinemaState : State {
    data object Init : CinemaState()
    data object Loading : CinemaState()
    data class OnScreen(val movies: List<Movie>, val dateParams: DateParameters) : CinemaState()
    data class Schedule(val date: LocalDate, val schedule: List<ScheduleEntry>) : CinemaState()
    data class Details(val date: LocalDate, val movie: Movie) : CinemaState()
    data class Error(val error: Throwable) : CinemaState()
}

data class PromotionalEffect(val url: String) : Effect
