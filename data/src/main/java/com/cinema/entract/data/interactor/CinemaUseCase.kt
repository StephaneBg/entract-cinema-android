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
import com.cinema.entract.data.source.CinemaDataStore
import org.threeten.bp.LocalDate

class CinemaUseCase(
    private val dataStore: CinemaDataStore,
    private val networkUtils: NetworkUtils
) {

    private var currentDate: LocalDate? = null

    fun getDate(): LocalDate = currentDate ?: initDate()

    private fun initDate(): LocalDate {
        val now = LocalDate.now()
        currentDate = now
        return now
    }

    suspend fun getMovies(): List<MovieData> = dataStore
        .getMovies(getDate().formatToUTC())
        .map {
            if (!canDisplayMedia()) it.copy(
                coverUrl = "",
                teaserId = ""
            ) else it
        }

    suspend fun getDateRange(): DateRangeData = dataStore.getParameters()

    suspend fun getSchedule(): List<WeekData> = dataStore.getSchedule().filter { it.hasMovies }

    suspend fun getEventUrl(): String = if (isEventEnabled()) dataStore.getEventUrl() else ""

    fun selectDate(selectedDate: LocalDate) {
        currentDate = selectedDate
    }

    private fun canDisplayMedia(): Boolean =
        !dataStore.getUserPreferences().isOnlyOnWifi() || networkUtils.isConnectedOnWifi()

    fun isEventEnabled(): Boolean = dataStore.getUserPreferences().isEventEnabled()

    fun setEventPreference(enabled: Boolean) =
        dataStore.getUserPreferences().setEventPreference(enabled)

    fun isOnlyOnWifi(): Boolean = dataStore.getUserPreferences().isOnlyOnWifi()

    fun setOnlyOnWifi(onlyOnWifi: Boolean) =
        dataStore.getUserPreferences().setOnlyOnWifi(onlyOnWifi)
}