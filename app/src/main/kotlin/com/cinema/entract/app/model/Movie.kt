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

package com.cinema.entract.app.model

import org.threeten.bp.Duration
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime

data class Movie(
    val sessionId: String,
    val movieId: String,
    val title: String,
    val date: LocalDate,
    val schedule: LocalTime,
    val isThreeDimension: Boolean,
    val isOriginalVersion: Boolean,
    val isArtMovie: Boolean,
    val isUnderTwelve: Boolean,
    val isExplicitContent: Boolean,
    val coverUrl: String,
    val duration: Duration,
    val yearOfProduction: String,
    val genre: String,
    val director: String,
    val cast: String,
    val synopsis: String,
    val teaserId: String,
    val nextMovies: List<Movie>
)
