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

package com.cinema.entract.data.source

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.CacheRepo
import com.cinema.entract.data.repository.RemoteRepo

class CinemaDataStoreImpl(
    private val cacheRepo: CacheRepo,
    private val remoteRepo: RemoteRepo
) : CinemaDataStore {

    override suspend fun getMovies(date: String): List<MovieData> =
        cacheRepo.getMovies(date) ?: run {
            val movies = remoteRepo.getMovies(date)
            cacheRepo.cacheMovies(date, movies)
            movies
        }

    override suspend fun getSchedule(): List<WeekData> =
        cacheRepo.getSchedule() ?: run {
            val schedule = remoteRepo.getSchedule()
            cacheRepo.cacheSchedule(schedule)
            schedule
        }

    override suspend fun getParameters(): DateRangeData =
        cacheRepo.getDateRange() ?: run {
            val dateRange = remoteRepo.getDateRange()
            cacheRepo.cacheDateRange(dateRange)
            dateRange
        }

    override suspend fun getEventUrl(): String =
        cacheRepo.getEventUrl() ?: run {
            val url = remoteRepo.getEventUrl()
            cacheRepo.cacheEventUrl(url)
            url
        }

    override suspend fun registerNotifications(token: String) =
        remoteRepo.registerNotifications(token)

    override suspend fun tagSchedule() = remoteRepo.tagSchedule()

    override suspend fun tagEvent() = remoteRepo.tagEvent()

    override suspend fun tagDetails(date: String, id: String) = remoteRepo.tagDetails(date, id)
}
