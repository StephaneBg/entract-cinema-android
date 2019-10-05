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

package com.cinema.entract.app.ui.onscreen

import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.ListItemOnScreenMovieBinding
import com.cinema.entract.app.ext.displayPlaceHolder
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.model.Movie
import com.cinema.entract.core.widget.ItemAdapter

class MovieAdapter(
    private val movie: Movie,
    private val selection: (Movie) -> Unit
) : ItemAdapter(R.layout.list_item_on_screen_movie) {

    override fun onBindViewHolder(itemView: View) {
        val binding = ListItemOnScreenMovieBinding.bind(itemView)
        binding.cover.apply {
            if (movie.coverUrl.isNotEmpty()) load(movie.coverUrl) else displayPlaceHolder()
        }
        binding.title.text = movie.title
        binding.schedule.text = movie.schedule
        binding.duration.text = movie.duration
        binding.originalVersion.isVisible = movie.isOriginalVersion
        binding.threeDimension.isVisible = movie.isThreeDimension
        binding.underTwelve.isVisible = movie.isUnderTwelve
        binding.explicitContent.isVisible = movie.isExplicitContent
        binding.artMovie.isInvisible = !movie.isArtMovie
        binding.root.setOnClickListener { selection(movie) }
    }
}
