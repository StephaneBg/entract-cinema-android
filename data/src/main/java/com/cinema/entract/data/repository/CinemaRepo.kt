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

package com.cinema.entract.data.repository

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.source.CinemaDataStore

class CinemaRepo(
    private val dataStore: CinemaDataStore,
    private val userPreferences: UserPreferences
) {

    suspend fun getMovies(date: String): List<MovieData> = dataStore.getMovies(date)

    suspend fun getSchedule(): List<WeekData> = dataStore.getSchedule().filter { it.hasMovies }

    suspend fun getParameters(): DateRangeData = dataStore.getParameters()

    suspend fun getEventUrl(): String = dataStore.getEventUrl()

    suspend fun registerNotifications(token: String) = dataStore.registerNotifications(token)

    suspend fun tagSchedule() = dataStore.tagSchedule()

    suspend fun tagEvent() = dataStore.tagEvent()

    suspend fun tagDetails(date: String, id: String) = dataStore.tagDetails(date, id)

    fun getUserPreferences(): UserPreferences = userPreferences
}