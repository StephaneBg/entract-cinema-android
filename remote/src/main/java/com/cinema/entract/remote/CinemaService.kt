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
    fun getMovies(@Query("jour") day: String): Deferred<List<MovieRemote>>

    @GET("getProgramme.php")
    fun getSchedule(): Deferred<List<WeekRemote>>

    @GET("getParametres.php")
    fun getParameters(): Deferred<ParametersRemote>

    @GET("getLiensEvenement.php")
    fun getEvent(): Deferred<EventRemote>

    @PUT("registerPushNotifications")
    fun registerNotifications(
        @Query("type") type: String = "android",
        @Query("deviceToken") token: String
    ): Deferred<Unit>

    @PUT("updateStatistiques.php")
    fun tagSchedule(@Query("page") type: String = "page_programme"): Deferred<Unit>

    @PUT("updateStatistiques.php")
    fun tagEvent(@Query("page") type: String = "page_evt"): Deferred<Unit>

    @PUT("updateStatistiques.php")
    fun tagDetails(
        @Query("page") type: String = "page_detail",
        @Query("seance") sessionId: String
    ): Deferred<Unit>

    @PUT("updateStatistiques.php")
    fun tagCalendar(
        @Query("page") type: String = "ajout_cal",
        @Query("seance") sessionId: String
    ): Deferred<Unit>
}
