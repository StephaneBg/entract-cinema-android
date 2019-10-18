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

package com.cinema.entract.data.repository

import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData

interface RemoteRepo {

    suspend fun getMovies(date: String): List<MovieData>

    suspend fun getSchedule(): List<WeekData>

    suspend fun getDateRange(): DateRangeData?

    suspend fun getPromotionalUrl(): String

    suspend fun registerNotifications(token: String)

    suspend fun tagSchedule()

    suspend fun tagPromotional()

    suspend fun tagDetails(sessionId: String)

    suspend fun tagCalendar(sessionId: String)
}