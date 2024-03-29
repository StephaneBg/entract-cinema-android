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

import com.cinema.entract.data.model.WeekData
import com.cinema.entract.remote.model.WeekRemote
import java.time.LocalDate

class WeekMapper(private val mapper: DayMapper) : Mapper<WeekRemote, WeekData> {

    override fun mapToData(model: WeekRemote) = WeekData(
        LocalDate.parse(model.debutsemaine) ?: error("Unknown date"),
        LocalDate.parse(model.finsemaine) ?: error("Unknown date"),
        model.jours?.map { mapper.mapToData(it) } ?: emptyList(),
        model.filmsDisponibles ?: false
    )
}