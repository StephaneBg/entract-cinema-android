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

package com.cinema.entract.app.ui.movies

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.load
import com.cinema.entract.core.ext.color
import com.cinema.entract.core.ext.inflate
import com.cinema.entract.core.views.bindView

class MoviesAdapter(private val selection: (Movie, ImageView) -> Unit) :
    RecyclerView.Adapter<MoviesAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.list_item_movies))

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

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            itemView.apply {
                if (movie.coverUrl.isNotEmpty()) {
                    cover.load(movie.coverUrl)
                } else {
                    cover.scaleType = ImageView.ScaleType.CENTER
                    cover.setImageResource(R.drawable.ic_movie_black_24dp)
                    cover.setColorFilter(context.color(R.color.primary_500))
                    cover.setBackgroundColor(context.color(R.color.primary_50))
                }
                cover.transitionName = "cover_transition_$adapterPosition"
                title.text = movie.title
                schedule.text = context.getString(R.string.movies_schedule, movie.schedule)
                duration.text = context.getString(R.string.movies_duration, movie.duration)
                originalVersion.isVisible = movie.isOriginalVersion
                threeDimension.isVisible = movie.isThreeDimension
                underTwelve.isInvisible = !movie.isUnderTwelve
                setOnClickListener { selection(movie, cover) }
            }
        }
    }
}