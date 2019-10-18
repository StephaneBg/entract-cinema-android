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

package com.cinema.entract.remote

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.remote.mapper.DateRangeMapper
import com.cinema.entract.remote.mapper.MovieMapper
import com.cinema.entract.remote.mapper.PromotionalMapper
import com.cinema.entract.remote.mapper.WeekMapper

class CinemaRemoteRepo(
    private val service: CinemaService,
    private val movieMapper: MovieMapper,
    private val weekMapper: WeekMapper,
    private val dateRangeMapper: DateRangeMapper,
    private val promotionalMapper: PromotionalMapper
) : RemoteRepo {

    override suspend fun getMovies(date: String): List<MovieData> =
        service.getMovies(date).map { movieMapper.mapToData(it) }

    override suspend fun getSchedule(): List<WeekData> =
        service.getSchedule().map { weekMapper.mapToData(it) }

    override suspend fun getPromotionalUrl(): String =
        promotionalMapper.mapToData(service.getPromotional())

    override suspend fun getDateRange(): DateRangeData? = service.getParameters().periode?.let {
        dateRangeMapper.mapToData(it)
    }

    override suspend fun registerNotifications(token: String) {
        service.registerNotifications(token = token)
    }

    override suspend fun tagSchedule() {
        service.tagSchedule()
    }

    override suspend fun tagPromotional() {
        service.tagPromotional()
    }

    override suspend fun tagDetails(sessionId: String) {
        service.tagDetails(sessionId = sessionId)
    }

    override suspend fun tagCalendar(sessionId: String) {
        service.tagCalendar(sessionId = sessionId)
    }
}
