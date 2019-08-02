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

import androidx.lifecycle.viewModelScope
import com.cinema.entract.app.mapper.DateRangeMapper
import com.cinema.entract.app.mapper.MovieMapper
import com.cinema.entract.app.mapper.ScheduleMapper
import com.cinema.entract.app.model.DateRange
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.core.ui.BaseViewModel
import com.cinema.entract.core.ui.Event
import com.cinema.entract.data.ext.toEpochMilliSecond
import com.cinema.entract.data.interactor.CinemaUseCase
import dev.gumil.kaskade.Action
import dev.gumil.kaskade.Kaskade
import dev.gumil.kaskade.State
import dev.gumil.kaskade.coroutines.coroutines
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import timber.log.Timber

class CinemaViewModel(
    private val useCase: CinemaUseCase,
    private val movieMapper: MovieMapper,
    private val scheduleMapper: ScheduleMapper,
    private val dateRangeMapper: DateRangeMapper
) : BaseViewModel<CinemaAction, CinemaState>() {

    override val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        Timber.e(exception)
        process(CinemaAction.Error(exception))
    }

    override val stateContainer = Kaskade.create<CinemaAction, CinemaState>(CinemaState.Loading()) {
        coroutines(viewModelScope + exceptionHandler) {
            on<CinemaAction.RefreshMovies> {
                process(CinemaAction.LoadMovies(action.date))
                CinemaState.Loading()
            }

            on<CinemaAction.LoadMovies> {
                withContext(Dispatchers.IO) {
                    val movies = useCase.getMovies(action.date)
                    val dateRange = useCase.getDateRange()
                    val url = useCase.getEventUrl()
                    CinemaState.OnScreen(
                        movies.map { movieMapper.mapToUi(it) },
                        useCase.getDate(),
                        dateRangeMapper.mapToUi(dateRange),
                        url
                    )
                }
            }

            on<CinemaAction.RefreshSchedule> {
                process(CinemaAction.LoadSchedule)
                CinemaState.Loading()
            }

            on<CinemaAction.LoadSchedule> {
                withContext(Dispatchers.IO) {
                    val schedule = useCase.getSchedule()
                    CinemaState.Schedule(useCase.getDate(), scheduleMapper.mapToUi(schedule))
                }
            }

            on<CinemaAction.LoadDetails> {
                withContext(Dispatchers.IO) {
                    val retrievedMovie = useCase.getMovie(movieMapper.mapToData(action.movie))
                    CinemaState.Details(useCase.getDate(), movieMapper.mapToUi(retrievedMovie))
                }
            }

            on<CinemaAction.Error> {
                CinemaState.Error(action.error)
            }
        }
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

sealed class CinemaAction : Action {
    data class RefreshMovies(val date: LocalDate? = null) : CinemaAction()
    data class LoadMovies(val date: LocalDate? = null) : CinemaAction()
    object RefreshSchedule : CinemaAction()
    object LoadSchedule : CinemaAction()
    data class LoadDetails(val movie: Movie) : CinemaAction()
    data class Error(val error: Throwable?) : CinemaAction()
}

sealed class CinemaState : State {
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
