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

import com.cinema.entract.app.mapper.DateRangeMapper
import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.model.DateRange
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.core.ui.Event
import com.cinema.entract.core.ui.ScopedViewModel
import com.cinema.entract.data.ext.toEpochMilliSecond
import com.cinema.entract.data.interactor.CinemaUseCase
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import timber.log.Timber

class CinemaViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper,
    private val scheduleMapper: ScheduleMapper,
    private val dateRangeMapper: DateRangeMapper
) : ScopedViewModel<CinemaAction, CinemaState>() {

    private var eventUrl: Event<String?>? = null

    override suspend fun bindActions(action: CinemaAction) = when (action) {
        is CinemaAction.LoadMovies -> retrieveMovies(action.date)
        CinemaAction.LoadSchedule -> retrieveSchedule()
        is CinemaAction.LoadDetails -> retrieveDetails(action.movie)
    }

    private fun retrieveMovies(date: LocalDate? = null) {
        innerState.postValue(CinemaState.Loading())
        launchAsync(
            {
                val movies = useCase.getMovies(date)
                val dateRange = useCase.getDateRange()
                eventUrl ?: getEventUrl()
                innerState.postValue(
                    CinemaState.OnScreen(
                        movies.map { movieMapper.mapToUi(it) },
                        useCase.getDate(),
                        dateRangeMapper.mapToUi(dateRange),
                        eventUrl ?: Event(null)
                    )
                )
            },
            {
                Timber.e(it)
                innerState.postValue(CinemaState.Error(it))
            }
        )
    }

    private suspend fun getEventUrl(): Event<String?>? {
        val url = useCase.getEventUrl()
        eventUrl = Event(if (url.isNotEmpty()) url else null)
        return eventUrl
    }

    private fun retrieveDetails(movie: Movie) {
        innerState.postValue(CinemaState.Loading())
        launchAsync(
            {
                val retrievedMovie = useCase.getMovie(movieMapper.mapToData(movie))
                innerState.postValue(
                    CinemaState.Details(useCase.getDate(), movieMapper.mapToUi(retrievedMovie))
                )
            },
            {
                Timber.e(it)
                innerState.postValue(CinemaState.Error(it, movie))
            }
        )
    }

    private fun retrieveSchedule() {
        innerState.postValue(CinemaState.Loading())
        launchAsync(
            {
                val schedule = useCase.getSchedule()
                innerState.postValue(
                    CinemaState.Schedule(useCase.getDate(), scheduleMapper.mapToUi(schedule))
                )
            },
            {
                Timber.e(it)
                innerState.postValue(CinemaState.Error(it))
            }
        )
    }

    fun getSessionSchedule(movie: Movie): Pair<Long, Long> {
        val schedule = movie.schedule.split(":").map { Integer.parseInt(it) }
        var time = LocalTime.of(schedule[0], schedule[1])
        val beginTime = LocalDateTime.of(
            movie.date.year,
            movie.date.month.value,
            movie.date.dayOfMonth,
            time.hour,
            time.minute
        )

        val duration = movie.duration.split("h").map { Integer.parseInt(it) }
        time = time.plusHours(duration[0].toLong())
        time = time.plusMinutes(duration[1].toLong())
        val endTime = LocalDateTime.of(
            movie.date.year,
            movie.date.month.value,
            movie.date.dayOfMonth,
            time.hour,
            time.minute
        )

        return beginTime.toEpochMilliSecond() to endTime.toEpochMilliSecond()
    }
}

sealed class CinemaAction {
    data class LoadMovies(val date: LocalDate? = null) : CinemaAction()
    object LoadSchedule : CinemaAction()
    data class LoadDetails(val movie: Movie) : CinemaAction()
}

sealed class CinemaState {

    data class OnScreen(
        val movies: List<Movie>,
        val date: LocalDate,
        val dateRange: DateRange? = null,
        val eventUrl: Event<String?>
    ) : CinemaState()

    data class Schedule(val date: LocalDate, val schedule: List<ScheduleEntry>) : CinemaState()

    data class Details(val date: LocalDate, val movie: Movie) : CinemaState()

    data class Loading(val refresh: Boolean = false) : CinemaState()

    data class Error(val error: Throwable? = null, val movie: Movie? = null) : CinemaState()
}
