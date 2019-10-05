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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.FragmentOnScreenBinding
import com.cinema.entract.app.model.DateParameters
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaAction
import com.cinema.entract.app.ui.CinemaState
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.event.EventActivity
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.widget.GenericRecyclerViewAdapter
import com.cinema.entract.data.ext.isToday
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.ext.toUtcEpochMilliSecond
import com.cinema.entract.data.ext.toUtcLocalDate
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OnScreenFragment : BaseLceFragment() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private lateinit var datePickerDialog: MaterialDatePicker<Long>
    private val onScreenAdapter = GenericRecyclerViewAdapter()
    private lateinit var binding: FragmentOnScreenBinding
    private var dateParams: DateParameters? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnScreenBinding.inflate(inflater)
        initLce(binding.loadingView, binding.contentView, binding.errorView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding.contentView) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
            setAdapter(onScreenAdapter)
        }

        binding.fab.setOnClickListener { displayDatePicker() }

        observe(cinemaViewModel.state, ::renderState)

        savedInstanceState ?: cinemaViewModel.process(CinemaAction.RefreshMovies())
    }

    private fun renderState(state: CinemaState?) {
        when (state) {
            is CinemaState.Loading -> showLoading()
            is CinemaState.OnScreen -> {
                dateParams = state.dateParams
                setTitle(dateParams?.currentDate?.longFormatToUi())
                binding.fab.isVisible =
                    null != dateParams?.minimumDate && null != dateParams?.maximumDate
                updateMovies(state.movies)
                showContent()
                showEvent(state)
            }
            is CinemaState.Error -> manageError(state.error)
        }
    }

    private fun updateMovies(movies: List<Movie>) {
        val adapters = movies.map { MovieAdapter(it, ::onMovieSelected) }
        onScreenAdapter.updateItems(adapters)
        showContent()
    }

    private fun manageError(exception: Throwable?) {
        showError(exception) { cinemaViewModel.process(CinemaAction.RefreshMovies()) }
        setTitle(R.string.app_name)
    }

    private fun onMovieSelected(movie: Movie) {
        cinemaViewModel.process(CinemaAction.LoadDetails(movie))
        findNavController().navigate(R.id.action_onScreenFragment_to_detailsFragment)
    }

    private fun displayDatePicker() {
        dateParams?.let {
            if (null != it.minimumDate && null != it.maximumDate) {
                val currentMilliSec = it.currentDate.toUtcEpochMilliSecond()
                val minMilliSec = it.minimumDate.toUtcEpochMilliSecond()
                val maxMilliSec = it.maximumDate.toUtcEpochMilliSecond()
                datePickerDialog = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(R.string.on_screen_date_picker_title)
                    .setSelection(currentMilliSec)
                    .setCalendarConstraints(
                        CalendarConstraints.Builder()
                            .setOpenAt(currentMilliSec)
                            .setStart(minMilliSec)
                            .setEnd(maxMilliSec)
                            .setValidator(CinemaDateValidator(minMilliSec, maxMilliSec))
                            .build()
                    )
                    .build()
                datePickerDialog.addOnPositiveButtonClickListener { selectionInMilliSec ->
                    val pickedDate = selectionInMilliSec.toUtcLocalDate()
                    cinemaViewModel.process(CinemaAction.RefreshMovies(pickedDate))
                }
                datePickerDialog.show(childFragmentManager, null)
            }
        }
    }

    private fun showEvent(state: CinemaState.OnScreen) {
        state.eventUrl.getContent()?.let {
            requireActivity().startActivity<EventActivity>(EventActivity.COVER_URL to it)
        }
    }

    fun isTodayDisplayed(): Boolean = dateParams?.currentDate?.isToday() == true
}
