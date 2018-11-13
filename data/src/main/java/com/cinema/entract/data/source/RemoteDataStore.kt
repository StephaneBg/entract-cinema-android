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
import com.cinema.entract.data.repository.RemoteRepo

class RemoteDataStore(private val remoteRepo: RemoteRepo) {

    suspend fun getMovies(date: String): List<MovieData> = remoteRepo.getMovies(date)

    suspend fun getSchedule(): List<WeekData> = remoteRepo.getSchedule()

    suspend fun getDateRange(): DateRangeData = remoteRepo.getDateRange()

    suspend fun getEventUrl(): String = remoteRepo.getEventUrl()

    suspend fun tagSchedule() = remoteRepo.tagSchedule()

    suspend fun tagEvent() = remoteRepo.tagEvent()

    suspend fun tagDetails(date: String, id: String) = remoteRepo.tagDetails(date, id)

    suspend fun registerNotifications(token: String) =
        remoteRepo.registerNotifications(token)
}