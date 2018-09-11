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

package com.cinema.entract.data.repository

import com.cinema.entract.data.model.MovieDataMapper
import com.cinema.entract.data.model.WeekDataMapper
import com.cinema.entract.data.source.CinemaDataStoreFactory
import com.cinema.entract.domain.model.MovieDomain
import com.cinema.entract.domain.model.WeekDomain
import com.cinema.entract.domain.repository.CinemaRepository

class CinemaRepositoryImpl(
    private val dataStoreFactory: CinemaDataStoreFactory,
    private val movieMapper: MovieDataMapper,
    private val weekMapper: WeekDataMapper
) : CinemaRepository {

    override suspend fun getMovies(day: String): List<MovieDomain> =
        dataStoreFactory.retrieveDataStore().getMovies(day).map { movieMapper.mapToDomain(it) }

    override suspend fun getSchedule(): List<WeekDomain> =
        dataStoreFactory.retrieveDataStore().getSchedule().map { weekMapper.mapToDomain(it) }
}