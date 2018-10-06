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
import com.cinema.entract.data.model.EventData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.CinemaRemote
import com.cinema.entract.remote.mapper.DateRangeRemoteMapper
import com.cinema.entract.remote.mapper.EventRemoteMapper
import com.cinema.entract.remote.mapper.MovieRemoteMapper
import com.cinema.entract.remote.mapper.WeekRemoteMapper
import timber.log.Timber

class CinemaRemoteImpl(
    private val service: CinemaService,
    private val movieMapper: MovieRemoteMapper,
    private val weekMapper: WeekRemoteMapper,
    private val paramMapper: DateRangeRemoteMapper,
    private val eventMapper: EventRemoteMapper
) : CinemaRemote {

    override suspend fun getMovies(day: String): List<MovieData> {
        Timber.d("Fetch movies")
        val movies = service.getMovies(day).await()
        return movies.map { movieMapper.mapToData(it) }
    }

    override suspend fun getSchedule(): List<WeekData> {
        Timber.d("Fetch schedule")
        val schedule = service.getSchedule().await()
        return schedule.map { weekMapper.mapToData(it) }
    }

    override suspend fun getEventUrl(): EventData {
        Timber.d("Fetch event")
        val event = service.getEvent().await()
        return eventMapper.mapToData(event)
    }

    override suspend fun getParameters(): DateRangeData {
        Timber.d("Fetch parameters")
        val parameters = service.getParameters().await()
        return parameters.periode?.let { paramMapper.mapToData(it) }
            ?: error("Incorrect server response")
    }
}