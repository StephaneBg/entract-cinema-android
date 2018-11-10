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

package com.cinema.entract.cache.mapper

import com.cinema.entract.cache.model.WeekCache
import com.cinema.entract.data.model.WeekData

class WeekMapper(private val mapper: DayMapper) : Mapper<WeekCache, WeekData> {

    override fun mapToData(model: WeekCache) = WeekData(
        model.beginDay,
        model.endDay,
        model.days.map { mapper.mapToData(it) },
        model.hasMovies
    )

    override fun mapFromData(model: WeekData) = WeekCache(
        model.beginDay,
        model.endDay,
        model.days.map { mapper.mapFromData(it) },
        model.hasMovies
    )
}