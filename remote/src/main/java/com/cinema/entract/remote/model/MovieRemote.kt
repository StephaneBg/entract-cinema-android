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

package com.cinema.entract.remote.model

import com.squareup.moshi.Json

data class MovieRemote(
    val id_film: String?,
    val titre: String?,
    val date: String?,
    val horaire: String?,
    @field:Json(name = "3d") val troisDimension: Boolean?,
    val vo: Boolean?,
    val moins_douze: Boolean?,
    val avertissement: Boolean?,
    val affiche: String?,
    val duree: String?,
    val annee: String?,
    val pays: String?,
    val style: String?,
    val de: String?,
    val avec: String?,
    val synopsis: String?,
    val bande_annonce: String?,
    val autres_dates: List<MovieRemote>?
)