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

package com.cinema.entract.app.ui.schedule

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.app.ui.CinemaAction
import com.cinema.entract.app.ui.NavAction
import com.cinema.entract.app.ui.NavOrigin
import com.cinema.entract.core.ui.bindView
import com.cinema.entract.core.widget.BaseViewHolder
import com.cinema.entract.core.widget.ItemAdapter

class DayHeaderAdapter(
    private val dayHeader: DayHeader,
    private val selection: (CinemaAction, NavAction) -> Unit
) : ItemAdapter(R.layout.list_item_schedule_day_header) {

    override fun BaseViewHolder.onBindViewHolder() {
        (itemView as TextView).text = dayHeader.dateUi
        itemView.setOnClickListener {
            selection(
                CinemaAction.LoadMovies(dayHeader.date),
                NavAction.OnScreen(NavOrigin.SCHEDULE)
            )
        }
    }
}

class WeekHeaderAdapter(private val weekHeader: WeekHeader) :
    ItemAdapter(R.layout.list_item_schedule_week_header) {

    override fun BaseViewHolder.onBindViewHolder() {
        (itemView as TextView).text = weekHeader.dateUi
    }
}

class MovieAdapter(
    private val model: MovieEntry,
    private val selection: (CinemaAction, NavAction) -> Unit
) : ItemAdapter(R.layout.list_item_schedule_movie) {

    private val schedule by bindView<TextView>(R.id.schedule)
    private val title by bindView<TextView>(R.id.title)
    private val originalVersion by bindView<ImageView>(R.id.originalVersion)
    private val threeDimension by bindView<ImageView>(R.id.threeDimension)
    private val underTwelve by bindView<ImageView>(R.id.underTwelve)
    private val explicitContent by bindView<ImageView>(R.id.explicitContent)
    private val artMovie by bindView<ImageView>(R.id.artMovie)

    override fun BaseViewHolder.onBindViewHolder() {
        schedule.text = model.movie.schedule
        title.text = model.movie.title
        originalVersion.isVisible = model.movie.isOriginalVersion
        threeDimension.isVisible = model.movie.isThreeDimension
        underTwelve.isVisible = model.movie.isUnderTwelve
        explicitContent.isVisible = model.movie.isExplicitContent
        artMovie.isVisible = model.movie.isArtMovie
        itemView.setOnClickListener {
            selection(CinemaAction.LoadDetails(model.movie), NavAction.Details)
        }
    }
}