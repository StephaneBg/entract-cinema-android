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

package com.cinema.entract.remote.mapper

import com.cinema.entract.data.model.DayData
import com.cinema.entract.remote.model.DayRemote
import java.time.LocalDate

class DayMapper(private val mapper: MovieMapper) : Mapper<DayRemote, DayData> {

    override fun mapToData(model: DayRemote) = DayData(
        LocalDate.parse(model.jour) ?: LocalDate.now(),
        model.films?.map { mapper.mapToData(it) } ?: emptyList()
    )
}