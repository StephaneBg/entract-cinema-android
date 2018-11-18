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

package com.cinema.entract.remote

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.remote.mapper.DateRangeMapper
import com.cinema.entract.remote.mapper.EventMapper
import com.cinema.entract.remote.mapper.MovieMapper
import com.cinema.entract.remote.mapper.WeekMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RemoteRepoImpl(
    private val service: CinemaService,
    private val movieMapper: MovieMapper,
    private val weekMapper: WeekMapper,
    private val dateRangeMapper: DateRangeMapper,
    private val eventMapper: EventMapper
) : RemoteRepo {

    override suspend fun getMovies(date: String): List<MovieData> = withContext(Dispatchers.IO) {
        val movies = service.getMovies(date).await()
        movies.map { movieMapper.mapToData(it) }
    }

    override suspend fun getSchedule(): List<WeekData> = withContext(Dispatchers.IO) {
        val schedule = service.getSchedule().await()
        schedule.map { weekMapper.mapToData(it) }
    }

    override suspend fun getEventUrl(): String = withContext(Dispatchers.IO) {
        val event = service.getEvent().await()
        eventMapper.mapToData(event)
    }

    override suspend fun getDateRange(): DateRangeData = withContext(Dispatchers.IO) {
        val parameters = service.getParameters().await()
        parameters.periode?.let { dateRangeMapper.mapToData(it) }
            ?: error("Incorrect server response")
    }

    override suspend fun registerNotifications(token: String) = withContext(Dispatchers.IO) {
        service.registerNotifications(token = token).await()
    }

    override suspend fun tagSchedule() = withContext(Dispatchers.IO) {
        service.tagSchedule().await()
    }

    override suspend fun tagEvent() = withContext(Dispatchers.IO) {
        service.tagEvent().await()
    }

    override suspend fun tagDetails(date: String, id: String) = withContext(Dispatchers.IO) {
        service.tagDetails(date = date, id = id).await()
    }
}