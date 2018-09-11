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

import com.cinema.entract.remote.model.MovieRemote
import com.cinema.entract.remote.model.WeekRemote
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import kotlinx.coroutines.experimental.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CinemaService {

    @GET("getFilmsJour.php")
    fun getMovies(@Query("jour") day: String): Deferred<List<MovieRemote>>

    @GET("getProgramme.php")
    fun getSchedule(): Deferred<List<WeekRemote>>
}

fun createService(): CinemaService = Retrofit.Builder()
    .baseUrl("http://mobile-grenadecinema.fr/php/rest/")
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create())
    .build()
    .create(CinemaService::class.java)