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

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.displayPlaceHolder
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.model.Movie
import com.cinema.entract.core.widget.BaseViewHolder
import com.cinema.entract.core.widget.ItemAdapter
import org.jetbrains.anko.find

class MovieAdapter(
    private val movie: Movie,
    private val selection: (Movie) -> Unit
) : ItemAdapter(R.layout.list_item_on_screen_movie) {

    override fun BaseViewHolder.onBindViewHolder() = with(itemView) {
        find<ImageView>(R.id.cover).apply {
            if (movie.coverUrl.isNotEmpty()) load(movie.coverUrl) else displayPlaceHolder()
        }
        find<TextView>(R.id.title).text = movie.title
        find<TextView>(R.id.schedule).text = movie.schedule
        find<TextView>(R.id.duration).text = movie.duration
        find<ImageView>(R.id.originalVersion).isVisible = movie.isOriginalVersion
        find<ImageView>(R.id.threeDimension).isVisible = movie.isThreeDimension
        find<ImageView>(R.id.underTwelve).isVisible = movie.isUnderTwelve
        find<ImageView>(R.id.explicitContent).isVisible = movie.isExplicitContent
        find<ImageView>(R.id.artMovie).isInvisible = !movie.isArtMovie
        setOnClickListener { selection(movie) }
    }
}