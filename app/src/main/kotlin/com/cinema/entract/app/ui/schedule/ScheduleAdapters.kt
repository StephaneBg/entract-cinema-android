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

package com.cinema.entract.app.ui.schedule

import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.ListItemScheduleMovieBinding
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.core.widget.ItemAdapter
import org.threeten.bp.LocalDate

class DayHeaderAdapter(
    private val dayHeader: DayHeader,
    private val selection: (LocalDate, Int) -> Unit
) : ItemAdapter(R.layout.list_item_schedule_day_header) {

    override fun onBindViewHolder(itemView: View) {
        with(itemView as TextView) {
            text = dayHeader.dateUi
            setOnClickListener {
                selection(
                    dayHeader.date,
                    R.id.action_scheduleFragment_to_onScreenFragment
                )
            }
        }
    }
}

class WeekHeaderAdapter(private val weekHeader: WeekHeader) :
    ItemAdapter(R.layout.list_item_schedule_week_header) {

    override fun onBindViewHolder(itemView: View) {
        (itemView as TextView).text = weekHeader.dateUi
    }
}

class MovieAdapter(
    private val model: MovieEntry,
    private val selection: (Movie, Int) -> Unit
) : ItemAdapter(R.layout.list_item_schedule_movie) {

    override fun onBindViewHolder(itemView: View) {
        with(ListItemScheduleMovieBinding.bind(itemView)) {
            schedule.text = model.movie.schedule
            title.text = model.movie.title
            originalVersion.isVisible = model.movie.isOriginalVersion
            threeDimension.isVisible = model.movie.isThreeDimension
            underTwelve.isVisible = model.movie.isUnderTwelve
            explicitContent.isVisible = model.movie.isExplicitContent
            artMovie.isVisible = model.movie.isArtMovie
            root.setOnClickListener {
                selection(
                    model.movie,
                    R.id.action_scheduleFragment_to_detailsFragment
                )
            }
        }
    }
}
