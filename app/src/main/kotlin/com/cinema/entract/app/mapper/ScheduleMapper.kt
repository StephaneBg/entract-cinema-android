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

package com.cinema.entract.app.mapper

import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.data.ext.isTodayOrLater
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.ext.sameMonth
import com.cinema.entract.data.ext.shortFormatToUi
import com.cinema.entract.data.model.WeekData

class ScheduleMapper(private val mapper: MovieMapper) :
    Mapper<List<ScheduleEntry>, List<WeekData>> {

    override fun mapToUi(model: List<WeekData>): List<ScheduleEntry> {
        val list = mutableListOf<ScheduleEntry>()
        model.forEach { week ->
            val entries = mutableListOf<ScheduleEntry>()
            week.days
                .filter { it.movies.isNotEmpty() }
                .filter { it.date.isTodayOrLater() }
                .forEach { day ->
                    entries.add(DayHeader(day.date.longFormatToUi(), day.date))

                    day.movies.forEach { movie ->
                        entries.add(MovieEntry(mapper.mapToUi(movie), day.date))
                    }
                }
            if (entries.isNotEmpty()) {
                list.add(WeekHeader(formatWeekHeader(week)))
                list.addAll(entries)
            }
        }
        return list
    }

    override fun mapToData(model: List<ScheduleEntry>): List<WeekData> =
        error(NotImplementedError())

    private fun formatWeekHeader(week: WeekData): String =
        if (week.beginDay sameMonth week.endDay) {
            "Du ${week.beginDay.dayOfMonth} au ${week.endDay.shortFormatToUi()}"
        } else {
            "Du ${week.beginDay.shortFormatToUi()} au ${week.endDay.shortFormatToUi()}"
        }
}