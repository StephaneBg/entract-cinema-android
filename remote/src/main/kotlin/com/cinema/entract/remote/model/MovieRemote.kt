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

package com.cinema.entract.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieRemote(
    val id_seance: String? = null,
    val id_film: String? = null,
    val titre: String? = null,
    val date: String? = null,
    val horaire: String? = null,
    @SerialName("3d") val troisDimension: Boolean? = null,
    val vo: Boolean? = null,
    val art_essai: Boolean? = null,
    val moins_douze: Boolean? = null,
    val avertissement: Boolean? = null,
    val affiche: String? = null,
    val duree: String? = null,
    val annee: String? = null,
    val pays: String? = null,
    val style: String? = null,
    val de: String? = null,
    val avec: String? = null,
    val synopsis: String? = null,
    val bande_annonce: String? = null,
    val autres_dates: List<MovieRemote>? = null
)