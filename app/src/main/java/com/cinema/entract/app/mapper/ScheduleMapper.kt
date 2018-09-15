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

package com.cinema.entract.app.mapper

import com.cinema.entract.app.ext.formatToUi
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.data.model.WeekData
import org.threeten.bp.LocalDate

class ScheduleMapper(private val mapper: MovieMapper) :
    Mapper<List<ScheduleEntry>, List<WeekData>> {

    override fun mapToUi(model: List<WeekData>): List<ScheduleEntry> {
        val list = mutableListOf<ScheduleEntry>()
        model.forEach { week ->
            list.add(WeekHeader("Du ${week.beginDay} au ${week.endDay}"))

            week.days.forEach { day ->
                list.add(DayHeader(LocalDate.parse(day.date).formatToUi()))

                day.movies.forEach { movie ->
                    list.add(MovieEntry(mapper.mapToUi(movie), day.date))
                }
            }
        }

        return list
    }
}