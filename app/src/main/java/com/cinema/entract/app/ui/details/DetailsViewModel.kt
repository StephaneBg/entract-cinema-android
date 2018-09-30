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

package com.cinema.entract.app.ui.details

import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.base.BaseViewModel
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import java.util.*

class DetailsViewModel : BaseViewModel() {

    fun getEventSchedule(movie: Movie): Pair<Long, Long> {
        val schedule = movie.schedule.split(":").map { Integer.parseInt(it) }
        var time = LocalTime.of(schedule[0], schedule[1])
        val beginTime = LocalDateTime.of(
            movie.date.year,
            movie.date.month.value - 1,
            movie.date.dayOfMonth,
            time.hour,
            time.minute
        )

        val duration = movie.duration.split("h").map { Integer.parseInt(it) }
        time = time.plusHours(duration[0].toLong())
        time = time.plusMinutes(duration[1].toLong())
        val endTime = LocalDateTime.of(
            movie.date.year,
            movie.date.month.value - 1,
            movie.date.dayOfMonth,
            time.hour,
            time.minute
        )

        return beginTime.toCalendar().timeInMillis to endTime.toCalendar().timeInMillis
    }

    private fun LocalDateTime.toCalendar(): Calendar {
        val calendar = Calendar.getInstance()
        calendar.set(this.year, this.month.value, this.dayOfMonth, this.hour, this.minute)
        return calendar
    }
}