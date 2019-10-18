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

package com.cinema.entract.cache.repository

import com.cinema.entract.cache.mapper.DateRangeMapper
import com.cinema.entract.cache.mapper.MovieMapper
import com.cinema.entract.cache.mapper.WeekMapper
import com.cinema.entract.cache.model.DateRangeCache
import com.cinema.entract.cache.model.MovieCache
import com.cinema.entract.cache.model.WeekCache
import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.CacheRepo

class CinemaCacheRepo(
    private val movieMapper: MovieMapper,
    private val weekMapper: WeekMapper,
    private val dateRangeMapper: DateRangeMapper
) : CacheRepo {

    private val moviesMap = mutableMapOf<String, List<MovieCache>>()
    private var schedule: List<WeekCache>? = null
    private var dateRange: DateRangeCache? = null
    private var promotionalUrl: String? = null

    override fun getMovies(date: String): List<MovieData>? = moviesMap[date]?.let {
        it.map { movie -> movieMapper.mapToData(movie) }
    }

    override fun cacheMovies(date: String, movies: List<MovieData>): List<MovieData> = movies.also {
        moviesMap[date] = movies.map { movieMapper.mapFromData(it) }
    }

    override fun getSchedule(): List<WeekData>? = schedule?.map { weekMapper.mapToData(it) }

    override fun cacheSchedule(weeks: List<WeekData>): List<WeekData> = weeks.also {
        schedule = weeks.map { weekMapper.mapFromData(it) }
    }

    override fun getDateRange(): DateRangeData? = dateRange?.let {
        dateRangeMapper.mapToData(it)
    }

    override fun cacheDateRange(range: DateRangeData?): DateRangeData? = range.also {
        range?.let { dateRange = dateRangeMapper.mapFromData(it) }
    }

    override fun getPromotionalUrl(): String? = promotionalUrl

    override fun cachePromotionalUrl(url: String): String = url.also { promotionalUrl = url }
}