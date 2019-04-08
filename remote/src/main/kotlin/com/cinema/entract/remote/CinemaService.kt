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

package com.cinema.entract.remote

import com.cinema.entract.remote.model.EventRemote
import com.cinema.entract.remote.model.MovieRemote
import com.cinema.entract.remote.model.ParametersRemote
import com.cinema.entract.remote.model.WeekRemote
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface CinemaService {

    @GET("getFilmsJour.php")
    suspend fun getMovies(@Query("jour") day: String): List<MovieRemote>

    @GET("getProgramme.php")
    suspend fun getSchedule(): List<WeekRemote>

    @GET("getParametres.php")
    suspend fun getParameters(): ParametersRemote

    @GET("getLiensEvenement.php")
    suspend fun getEvent(): EventRemote

    @PUT("registerPushNotifications")
    suspend fun registerNotifications(
        @Query("type") type: String = "android",
        @Query("deviceToken") token: String
    )

    @PUT("updateStatistiques.php")
    suspend fun tagSchedule(@Query("page") type: String = "page_programme")

    @PUT("updateStatistiques.php")
    suspend fun tagEvent(@Query("page") type: String = "page_evt")

    @PUT("updateStatistiques.php")
    suspend fun tagDetails(
        @Query("page") type: String = "page_detail",
        @Query("seance") sessionId: String
    )

    @PUT("updateStatistiques.php")
    suspend fun tagCalendar(
        @Query("page") type: String = "ajout_cal",
        @Query("seance") sessionId: String
    )
}
