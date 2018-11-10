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

package com.cinema.entract.app.ui.details

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.provider.CalendarContract.Events
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.TagViewModel
import com.cinema.entract.core.ext.color
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ext.inflate
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ext.toSpanned
import com.cinema.entract.core.ui.BaseFragment
import com.cinema.entract.data.ext.formatToUTC
import com.cinema.entract.data.ext.longFormatToUi
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailsFragment : BaseFragment() {

    private val detailsViewModel by viewModel<DetailsViewModel>()
    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val tagViewModel by sharedViewModel<TagViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(cinemaViewModel.getDetailedMovie(), ::displayMovieDetails)
    }

    private fun displayMovieDetails(movie: Movie?) {
        movie ?: error("No selected movie")

        tagViewModel.tagDetails(movie.date.formatToUTC(), movie.id)

        find<TextView>(R.id.dateTime).text = getString(
            R.string.details_date_with_time,
            movie.date.longFormatToUi(),
            movie.schedule
        )
        find<ImageView>(R.id.cover).apply {
            if (movie.coverUrl.isNotEmpty()) {
                load(movie.coverUrl)
            } else {
                scaleType = ImageView.ScaleType.CENTER
                setImageResource(R.drawable.ic_movie_black_24dp)
                setColorFilter(context.color(R.color.primary))
                setBackgroundColor(context.color(R.color.primary_light))
            }
        }
        find<TextView>(R.id.title).text = movie.title
        find<ImageView>(R.id.originalVersion).isVisible = movie.isOriginalVersion
        find<ImageView>(R.id.threeDimension).isVisible = movie.isThreeDimension
        find<ImageView>(R.id.underTwelve).isVisible = movie.isUnderTwelve
        find<ImageView>(R.id.explicitContent).isVisible = movie.isExplicitContent
        find<TextView>(R.id.director).text =
                getString(R.string.details_director, movie.director).toSpanned()
        with(find<TextView>(R.id.cast)) {
            if (movie.cast.isEmpty()) isVisible = false
            else text = getString(R.string.details_cast, movie.cast).toSpanned()
        }
        find<TextView>(R.id.year).text =
                getString(R.string.details_production_year, movie.yearOfProduction).toSpanned()
        find<TextView>(R.id.duration).text = getString(R.string.details_duration, movie.duration)
        find<TextView>(R.id.genre).text = movie.genre

        with(find<TextView>(R.id.synopsis)) {
            text = movie.synopsis
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
            }
        }

        manageNextMovies(movie.nextMovies)

        with(find<Button>(R.id.teaser)) {
            if (movie.teaserId.isNotEmpty()) {
                setOnClickListener { showTeaser(movie) }
            } else {
                isVisible = false
            }
        }
        find<Button>(R.id.agenda).setOnClickListener { addCalendarEvent(movie) }
    }

    private fun manageNextMovies(movies: List<Movie>) {
        val container = find<LinearLayout>(R.id.nextContainer)
        if (movies.isEmpty()) {
            container.inflate(R.layout.list_item_details_movie, true)
                .find<TextView>(R.id.dateSchedule).setText(R.string.details_no_next_movies)
        } else {
            movies.forEach { movie ->
                with(container.inflate(R.layout.list_item_details_movie, true)) {
                    find<TextView>(R.id.dateSchedule).text = getString(
                        R.string.details_date_with_time,
                        movie.date.longFormatToUi(),
                        movie.schedule
                    )
                    find<ImageView>(R.id.originalVersion).isVisible = movie.isOriginalVersion
                    find<ImageView>(R.id.threeDimension).isVisible = movie.isThreeDimension
                    setOnClickListener {
                        cinemaViewModel.retrieveMovies(movie.date)
                        (activity as CinemaActivity).selectOnScreen()
                    }
                }
            }
        }
    }

    private fun showTeaser(movie: Movie) {
        try {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "vnd.youtube:${movie.teaserId}".toUri()
                )
            )
        } catch (ex: ActivityNotFoundException) {
            requireContext().startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "http://www.youtube.com/watch?v=${movie.teaserId}".toUri()
                )
            )
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
            .putExtra(Events.EVENT_LOCATION, getString(R.string.information_address))
            .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_BUSY)
        startActivity(intent)
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }
}