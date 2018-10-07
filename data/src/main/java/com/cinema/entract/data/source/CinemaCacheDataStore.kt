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
import com.cinema.entract.data.repository.CinemaCache

class CinemaCacheDataStore(private val cinemaCache: CinemaCache) : CinemaDataStore {

    override suspend fun getMovies(day: String): List<MovieData> {
        TODO("not implemented")
    }

    override suspend fun getSchedule(): List<WeekData> {
        TODO("not implemented")
    }

    override suspend fun getParameters(): DateRangeData {
        TODO("not implemented")
    }

    override suspend fun getEventUrl(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}