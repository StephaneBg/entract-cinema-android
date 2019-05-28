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

package com.cinema.entract.data.source

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.CacheRepo
import com.cinema.entract.data.repository.RemoteRepo
import com.cinema.entract.data.repository.UserPreferencesRepo

class CinemaDataStoreImpl(
    private val cacheRepo: CacheRepo,
    private val remoteRepo: RemoteRepo,
    private val userPreferencesRepo: UserPreferencesRepo
) : CinemaDataStore {

    override suspend fun getMovies(date: String): List<MovieData> =
        cacheRepo.getMovies(date) ?: cacheRepo.cacheMovies(date, remoteRepo.getMovies(date))

    override suspend fun getSchedule(): List<WeekData> =
        cacheRepo.getSchedule() ?: cacheRepo.cacheSchedule(remoteRepo.getSchedule())

    override suspend fun getDateRange(): DateRangeData =
        cacheRepo.getDateRange() ?: cacheRepo.cacheDateRange(remoteRepo.getDateRange())

    override suspend fun getEventUrl(): String =
        cacheRepo.getEventUrl() ?: cacheRepo.cacheEventUrl(remoteRepo.getEventUrl())

    override suspend fun registerNotifications(token: String) =
        remoteRepo.registerNotifications(token)

    override suspend fun tagSchedule() = remoteRepo.tagSchedule()

    override suspend fun tagEvent() = remoteRepo.tagEvent()

    override suspend fun tagDetails(sessionId: String) = remoteRepo.tagDetails(sessionId)

    override suspend fun tagCalendar(sessionId: String) = remoteRepo.tagCalendar(sessionId)

    override fun getUserPreferences(): UserPreferencesRepo = userPreferencesRepo
}
