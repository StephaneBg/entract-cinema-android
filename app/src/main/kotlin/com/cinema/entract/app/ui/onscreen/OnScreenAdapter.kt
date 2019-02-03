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

package com.cinema.entract.app.ui.onscreen

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.displayPlaceHolder
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.model.Movie
import com.cinema.entract.core.ext.inflate
import com.cinema.entract.core.ui.bindView

class OnScreenAdapter(private val selection: (Movie) -> Unit) :
    RecyclerView.Adapter<OnScreenAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.list_item_on_screen_movie))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cover by bindView<ImageView>(R.id.cover)
        private val title by bindView<TextView>(R.id.title)
        private val schedule by bindView<TextView>(R.id.schedule)
        private val duration by bindView<TextView>(R.id.duration)
        private val originalVersion by bindView<ImageView>(R.id.originalVersion)
        private val threeDimension by bindView<ImageView>(R.id.threeDimension)
        private val underTwelve by bindView<ImageView>(R.id.underTwelve)
        private val explicitContent by bindView<ImageView>(R.id.explicitContent)
        private val artMovie by bindView<ImageView>(R.id.artMovie)

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            itemView.apply {
                cover.apply {
                    if (movie.coverUrl.isNotEmpty()) load(movie.coverUrl) else displayPlaceHolder()
                }
                title.text = movie.title
                schedule.text = context.getString(R.string.on_screen_schedule, movie.schedule)
                duration.text = context.getString(R.string.on_screen_duration, movie.duration)
                originalVersion.isVisible = movie.isOriginalVersion
                threeDimension.isVisible = movie.isThreeDimension
                underTwelve.isVisible = movie.isUnderTwelve
                explicitContent.isVisible = movie.isExplicitContent
                artMovie.isInvisible = !movie.isArtMovie
                setOnClickListener { selection(movie) }
            }
        }
    }
}
