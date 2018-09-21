/*
 * Copyright 2018 Stéphane Baiget
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.cinema.entract.remote

import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.repository.CinemaRemote
import com.cinema.entract.remote.model.MovieRemote
import com.cinema.entract.remote.model.RemoteMapper

class CinemaRemoteImpl(
    private val service: CinemaService,
    private val mapper: RemoteMapper<MovieRemote, MovieData>
) : CinemaRemote {

    override suspend fun getMovies(day: String): List<MovieData> {
        val movies = service.getMovies(day).await()
        return movies.map { mapper.mapFromRemote(it) }
    }
}