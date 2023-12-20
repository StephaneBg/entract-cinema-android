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

package com.cinema.entract.app.ui.details

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.text.LineBreaker
import android.os.Build
import android.os.Bundle
import android.provider.CalendarContract
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.ActivityDetailsBinding
import com.cinema.entract.app.databinding.ListItemDetailsMovieBinding
import com.cinema.entract.app.ext.displayPlaceHolder
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaState
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.TagAction
import com.cinema.entract.app.ui.TagViewModel
import com.cinema.entract.core.ext.toSpanned
import com.cinema.entract.core.ui.BaseLceActivity
import com.cinema.entract.data.ext.formatToUi
import com.cinema.entract.data.ext.longFormatToUi
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class DetailsActivity : BaseLceActivity() {

    private val cinemaViewModel by viewModel<CinemaViewModel>()
    private val tagViewModel by viewModel<TagViewModel>()
    private lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initLce(binding.loadingView, binding.contentView, binding.errorView)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cinemaViewModel.observeStates(this) { state ->
            when (state) {
                is CinemaState.Loading -> showLoading()
                is CinemaState.Error -> {
                    setTitle(R.string.app_name)
                    showError(state.error) {
                        cinemaViewModel.loadMovieDetails()
                    }
                }

                is CinemaState.Details -> {
                    val movie = state.movie
                    tagViewModel.tag(TagAction.Details(movie.sessionId))

                    title = getString(
                        R.string.details_date_with_time,
                        movie.date.longFormatToUi(),
                        movie.schedule
                    )
                    binding.cover.apply {
                        if (movie.coverUrl.isNotEmpty()) load(movie.coverUrl)
                        else displayPlaceHolder()
                    }
                    binding.title.text = movie.title
                    binding.originalVersion.isVisible = movie.isOriginalVersion
                    binding.threeDimension.isVisible = movie.isThreeDimension
                    binding.underTwelve.isVisible = movie.isUnderTwelve
                    binding.underTwelveNotice.isVisible = movie.isUnderTwelve
                    binding.explicitContent.isVisible = movie.isExplicitContent
                    binding.artMovie.isVisible = movie.isArtMovie
                    binding.explicitContentNotice.isVisible = movie.isExplicitContent
                    binding.director.text = getString(
                        R.string.details_director,
                        movie.director
                    ).toSpanned()
                    if (movie.cast.isEmpty()) binding.cast.isVisible = false
                    else binding.cast.text =
                        getString(R.string.details_cast, movie.cast).toSpanned()
                    binding.year.text = getString(
                        R.string.details_production_year,
                        movie.yearOfProduction
                    ).toSpanned()
                    binding.duration.text = movie.duration.formatToUi()
                    binding.genre.text = movie.genre

                    with(binding.synopsis) {
                        text = movie.synopsis
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            justificationMode = LineBreaker.JUSTIFICATION_MODE_INTER_WORD
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            @SuppressLint("WrongConstant")
                            justificationMode = Layout.JUSTIFICATION_MODE_INTER_WORD
                        }
                    }

                    manageNextMovies(movie.nextMovies)

                    with(binding.teaser) {
                        if (movie.teaserId.isNotEmpty()) {
                            setOnClickListener { showTeaser(movie) }
                        } else {
                            isVisible = false
                        }
                    }
                    binding.agenda.setOnClickListener { addCalendarEvent(movie) }
                    showContent()
                }
            }
        }

        cinemaViewModel.loadMovieDetails()
    }

    override fun onUpPressed() {
        finish()
    }

    private fun manageNextMovies(movies: List<Movie>) {
        val container = binding.nextContainer
        if (movies.isEmpty()) {
            val listItem = inflateNextListItem(container)
            listItem.dateSchedule.setText(R.string.details_no_next_movies)
        } else {
            movies.forEach { movie ->
                val listItem = inflateNextListItem(container)
                listItem.dateSchedule.text = getString(
                    R.string.details_date_with_time,
                    movie.date.longFormatToUi(),
                    movie.schedule
                )
                listItem.originalVersion.isVisible = movie.isOriginalVersion
                listItem.threeDimension.isVisible = movie.isThreeDimension
                listItem.root.setOnClickListener {
                    cinemaViewModel.selectDate(movie.date)
                    cinemaViewModel.loadMovies()
                    finish()
                }
            }
        }
    }

    private fun inflateNextListItem(parent: ViewGroup): ListItemDetailsMovieBinding =
        ListItemDetailsMovieBinding.inflate(LayoutInflater.from(this), parent, true)

    private fun showTeaser(movie: Movie) {
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "vnd.youtube:${movie.teaserId}".toUri()
                )
            )
        } catch (ex: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    "http://www.youtube.com/watch?v=${movie.teaserId}".toUri()
                )
            )
        }
    }

    private fun addCalendarEvent(movie: Movie) {
        try {
            val (beginTime, endTime) = cinemaViewModel.getSessionSchedule(movie)
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime)
                .putExtra(CalendarContract.Events.TITLE, movie.title)
                .putExtra(CalendarContract.Events.DESCRIPTION, getString(R.string.app_name))
                .putExtra(
                    CalendarContract.Events.EVENT_LOCATION,
                    getString(R.string.information_address)
                )
                .putExtra(
                    CalendarContract.Events.AVAILABILITY,
                    CalendarContract.Events.AVAILABILITY_BUSY
                )
            tagViewModel.tag(TagAction.Calendar(movie.sessionId))
            startActivity(intent)
        } catch (e: Exception) {
            Timber.e(e)
            Toast.makeText(this, R.string.error_general, Toast.LENGTH_SHORT).show()
        }
    }
}
