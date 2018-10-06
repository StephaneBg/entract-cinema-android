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
import com.cinema.entract.data.model.EventData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.source.CinemaDataStoreFactory

class CinemaRepository(
    private val dataStoreFactory: CinemaDataStoreFactory,
    private val userPreferences: UserPreferences
) {

    suspend fun getMovies(day: String): List<MovieData> =
        dataStoreFactory.retrieveDataStore().getMovies(day)

    suspend fun getSchedule(): List<WeekData> =
        dataStoreFactory.retrieveDataStore().getSchedule().filter { it.hasMovies }

    suspend fun getParameters(): DateRangeData =
        dataStoreFactory.retrieveDataStore().getParameters()

    suspend fun getEvent(): EventData = dataStoreFactory.retrieveDataStore().getEventUrl()

    fun getUserPreferences(): UserPreferences = userPreferences
}