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

package com.cinema.entract.app.ui.details

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.base.BaseFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailsFragment : BaseFragment() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val detailsViewModel by viewModel<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(cinemaViewModel.getSelectedMovie(), ::displayMovie)
    }

    private fun displayMovie(movie: Movie?) {
        movie?.let {
            find<ImageView>(R.id.cover).load(movie.coverUrl)
            find<TextView>(R.id.title).text = movie.title
            find<TextView>(R.id.director).text = movie.director
            movie.cast.apply {
                val view = find<TextView>(R.id.cast)
                if (isEmpty()) view.isVisible = false
                else view.text = this
            }
            find<TextView>(R.id.year).text = movie.yearOfProduction
            find<TextView>(R.id.duration).text = movie.duration
            find<TextView>(R.id.genre).text = movie.genre
            find<TextView>(R.id.synopsis).text = movie.synopsis
            find<FloatingActionButton>(R.id.fab).setOnClickListener { _ -> addCalendarEvent(it) }
        }
    }

    private fun addCalendarEvent(movie: Movie) {
        val (beginTime, endTime) = detailsViewModel.getEventSchedule(movie)
        val intent = Intent(Intent.ACTION_INSERT)
            .setData(Events.CONTENT_URI)
            .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
            .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
            .putExtra(Events.TITLE, movie.title)
            .putExtra(Events.DESCRIPTION, getString(R.string.app_name))
            .putExtra(Events.EVENT_LOCATION, getString(R.string.direction_address))
            .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }
}