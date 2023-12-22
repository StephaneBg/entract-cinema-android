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

package com.cinema.entract.remote

import com.cinema.entract.remote.model.MovieRemote
import com.cinema.entract.remote.model.ParametersRemote
import com.cinema.entract.remote.model.PromotionalRemote
import com.cinema.entract.remote.model.WeekRemote
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class CinemaApi(private val client: HttpClient) {

    private val baseUrl: String = "http://mobile-grenadecinema.fr/php/rest"

    suspend fun getMovies(day: String): List<MovieRemote> =
        client.get("${baseUrl}/getFilmsJour.php") {
            url {
                parameters.append("jour", day)
            }
        }.body()

    suspend fun getSchedule(): List<WeekRemote> =
        client.get("${baseUrl}/getProgramme.php").body()

    suspend fun getParameters(): ParametersRemote =
        client.get("${baseUrl}/getParametres.php").body()

    suspend fun getPromotional(): PromotionalRemote =
        client.get("$baseUrl/getLiensEvenement.php").body()

    suspend fun registerNotifications(type: String = "android", token: String): HttpResponse =
        client.put("$baseUrl/registerPushNotifications") {
            url {
                parameters.append("type", type)
                parameters.append("deviceToken", token)
            }
        }

    suspend fun tag(page: String, sessionId: String? = null): HttpResponse =
        client.put("$baseUrl/updateStatistiques.php") {
            url {
                parameters.append("page", page)
                sessionId?.let { parameters.append("seance", it) }
            }
        }
}

fun createClient(): HttpClient = HttpClient(Android) {
    install(ContentNegotiation) {
        json(
            Json {
                isLenient = true
                ignoreUnknownKeys = true
                explicitNulls = false
            },
            ContentType.Any
        )

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }
}

private const val TIME_OUT = 30_000
