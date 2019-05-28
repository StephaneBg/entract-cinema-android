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

package com.cinema.entract.cache.mapper

import com.cinema.entract.cache.model.DayCache
import com.cinema.entract.data.model.DayData

class DayMapper(private val mapper: MovieMapper) : Mapper<DayCache, DayData> {

    override fun mapToData(model: DayCache) = DayData(
        model.date,
        model.movies.map { mapper.mapToData(it) }
    )

    override fun mapFromData(model: DayData) = DayCache(
        model.date,
        model.movies.map { mapper.mapFromData(it) }
    )
}