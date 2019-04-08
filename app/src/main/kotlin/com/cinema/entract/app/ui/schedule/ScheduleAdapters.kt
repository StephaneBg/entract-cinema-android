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
import com.cinema.entract.core.widget.BaseViewHolder
import com.cinema.entract.core.widget.ItemAdapter
import org.jetbrains.anko.find

class DayHeaderAdapter(
    private val dayHeader: DayHeader,
    private val selection: (CinemaAction, NavAction) -> Unit
) : ItemAdapter(R.layout.list_item_schedule_day_header) {

    override fun BaseViewHolder.onBindViewHolder() = with(itemView as TextView) {
        text = dayHeader.dateUi
        setOnClickListener {
            selection(
                CinemaAction.LoadMovies(dayHeader.date),
                NavAction.OnScreen(NavOrigin.SCHEDULE)
            )
        }
    }
}

class WeekHeaderAdapter(private val weekHeader: WeekHeader) :
    ItemAdapter(R.layout.list_item_schedule_week_header) {

    override fun BaseViewHolder.onBindViewHolder() = with(itemView as TextView) {
        text = weekHeader.dateUi
    }
}

class MovieAdapter(
    private val model: MovieEntry,
    private val selection: (CinemaAction, NavAction) -> Unit
) : ItemAdapter(R.layout.list_item_schedule_movie) {

    override fun BaseViewHolder.onBindViewHolder() = with(itemView) {
        find<TextView>(R.id.schedule).text = model.movie.schedule
        find<TextView>(R.id.title).text = model.movie.title
        find<ImageView>(R.id.originalVersion).isVisible = model.movie.isOriginalVersion
        find<ImageView>(R.id.threeDimension).isVisible = model.movie.isThreeDimension
        find<ImageView>(R.id.underTwelve).isVisible = model.movie.isUnderTwelve
        find<ImageView>(R.id.explicitContent).isVisible = model.movie.isExplicitContent
        find<ImageView>(R.id.artMovie).isVisible = model.movie.isArtMovie
        setOnClickListener {
            selection(CinemaAction.LoadDetails(model.movie), NavAction.Details)
        }
    }
}