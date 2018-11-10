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

package com.cinema.entract.data.interactor

import com.cinema.entract.core.network.NetworkUtils
import com.cinema.entract.data.ext.formatToUTC
import com.cinema.entract.data.model.DateRangeData
import com.cinema.entract.data.model.MovieData
import com.cinema.entract.data.model.WeekData
import com.cinema.entract.data.repository.CinemaRepository
import org.threeten.bp.LocalDate

class CinemaUseCase(
    private val repo: CinemaRepository,
    private val networkUtils: NetworkUtils
) {

    private var date: LocalDate? = null
    private var dateRange: DateRangeData? = null
    private var eventUrl: String? = null

    fun getDate(): LocalDate = date ?: initDate()

    private fun initDate(): LocalDate {
        val now = LocalDate.now()
        date = now
        return now
    }

    suspend fun getMovies(): List<MovieData> = repo
        .getMovies(getDate().formatToUTC())
        .map {
            if (!canDisplayMedia()) it.copy(
                coverUrl = "",
                teaserId = ""
            ) else it
        }

    suspend fun getDateRange(): DateRangeData = dateRange ?: initDateRange()

    private suspend fun initDateRange(): DateRangeData {
        val range = repo.getParameters()
        dateRange = range
        return range
    }

    suspend fun getSchedule(): List<WeekData> = repo.getSchedule().filter { it.hasMovies }

    suspend fun getEventUrl(): String = eventUrl ?: initEventUrl()

    private suspend fun initEventUrl(): String {
        val url = repo.getEventUrl()
        eventUrl = url
        return url
    }

    fun selectDate(selectedDate: LocalDate) {
        date = selectedDate
    }

    private fun canDisplayMedia(): Boolean =
        !repo.getUserPreferences().isOnlyOnWifi() || networkUtils.isConnectedOnWifi()
}