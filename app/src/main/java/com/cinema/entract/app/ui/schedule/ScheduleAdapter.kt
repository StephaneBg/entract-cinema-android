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

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.core.ext.inflate
import com.cinema.entract.core.views.bindView
import org.threeten.bp.LocalDate

class ScheduleAdapter(private val selection: (LocalDate) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var schedule = emptyList<ScheduleEntry>()

    fun updateSchedule(newSchedule: List<ScheduleEntry>) {
        schedule = newSchedule
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_WEEK_HEADER -> WeekHeaderHolder(parent.inflate(R.layout.list_item_schedule_week_header))
            TYPE_DAY_HEADER -> DayHeaderHolder(parent.inflate(R.layout.list_item_schedule_day_header))
            TYPE_MOVIE -> MovieHolder(parent.inflate(R.layout.list_item_schedule_movie))
            else -> error("Unknown type")
        }

    override fun getItemCount(): Int = schedule.size

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        (holder as ScheduleViewHolder<ScheduleEntry>).bind(schedule[position])

    override fun getItemViewType(position: Int): Int = when (schedule[position]) {
        is WeekHeader -> TYPE_WEEK_HEADER
        is DayHeader -> TYPE_DAY_HEADER
        is MovieEntry -> TYPE_MOVIE
    }

    inner class WeekHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ScheduleViewHolder<WeekHeader> {
        override fun bind(model: WeekHeader) {
            (itemView as TextView).text = model.dateUi
        }
    }

    inner class DayHeaderHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ScheduleViewHolder<DayHeader> {
        override fun bind(model: DayHeader) {
            (itemView as TextView).text = model.dateUi
            itemView.setOnClickListener { selection(model.date) }
        }
    }

    inner class MovieHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        ScheduleViewHolder<MovieEntry> {

        private val schedule by bindView<TextView>(R.id.schedule)
        private val title by bindView<TextView>(R.id.title)
        private val originalVersion by bindView<ImageView>(R.id.originalVersion)
        private val threeDimension by bindView<ImageView>(R.id.threeDimension)
        private val underTwelve by bindView<ImageView>(R.id.underTwelve)

        override fun bind(model: MovieEntry) {
            schedule.text = model.movie.schedule
            title.text = model.movie.title
            originalVersion.isVisible = model.movie.isOriginalVersion
            threeDimension.isVisible = model.movie.isThreeDimension
            underTwelve.isVisible = model.movie.isUnderTwelve
            itemView.setOnClickListener { selection(model.date) }
        }
    }

    interface ScheduleViewHolder<T : ScheduleEntry> {
        fun bind(model: T)
    }

    companion object {
        private const val TYPE_WEEK_HEADER = 0
        private const val TYPE_DAY_HEADER = 1
        private const val TYPE_MOVIE = 2
    }
}