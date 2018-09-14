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

package com.cinema.entract.app.mapper

import com.cinema.entract.app.model.Movie
import com.cinema.entract.data.model.MovieData

class MovieMapper : Mapper<Movie, MovieData> {

    override fun mapToUi(model: MovieData) = Movie(
        model.title,
        model.date,
        model.schedule,
        model.threeDimension,
        model.originalVersion,
        model.coverUrl,
        model.duration,
        "Sortie en ${model.yearOfProduction}",
        model.genre,
        "De ${model.director}",
        "Avec ${model.cast}",
        "Synopsis\n${model.synopsis}",
        model.teaserUrl
    )
}