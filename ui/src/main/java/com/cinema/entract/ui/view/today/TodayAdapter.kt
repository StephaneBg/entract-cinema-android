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

package com.cinema.entract.ui.view.today

import android.annotation.SuppressLint
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.ui.R
import com.cinema.entract.ui.ext.inflate
import com.cinema.entract.ui.model.Movie
import org.jetbrains.anko.find

class TodayAdapter : RecyclerView.Adapter<TodayAdapter.ViewHolder>() {

    private var movies = emptyList<Movie>()

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(parent.inflate(R.layout.item_today_movie))

    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(movies[position])

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val cover = itemView.find<ImageView>(R.id.cover)
        private val title = itemView.find<TextView>(R.id.title)
        private val scheduleDuration = itemView.find<TextView>(R.id.scheduleDuration)
        private val originalVersion = itemView.find<TextView>(R.id.originalVersion)
        private val threeDimension = itemView.find<TextView>(R.id.threeDimension)

        @SuppressLint("SetTextI18n")
        fun bind(movie: Movie) {
            itemView.apply {
                cover.isVisible = false
                title.text = movie.title
                scheduleDuration.text = "${movie.schedule} - ${movie.duration}"
                originalVersion.isVisible = movie.isOriginalVersion
                threeDimension.isVisible = movie.isThreeDimension
            }
        }
    }
}