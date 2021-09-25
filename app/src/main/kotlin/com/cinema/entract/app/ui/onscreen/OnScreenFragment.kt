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
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.FragmentOnScreenBinding
import com.cinema.entract.app.model.DateParameters
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaEvent
import com.cinema.entract.app.ui.CinemaState
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.promotional.PromotionalActivity
import com.cinema.entract.core.ext.start
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.utils.EmptinessHelper
import com.cinema.entract.core.widget.GenericRecyclerViewAdapter
import com.cinema.entract.data.ext.isToday
import com.cinema.entract.data.ext.longFormatToUi
import com.cinema.entract.data.ext.toUtcEpochMilliSecond
import com.cinema.entract.data.ext.toUtcLocalDate
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import io.uniflow.android.livedata.onEvents
import io.uniflow.android.livedata.onStates
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OnScreenFragment : BaseLceFragment() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val onScreenAdapter = GenericRecyclerViewAdapter()
    private val emptinessHelper = EmptinessHelper()
    private var dateParams: DateParameters? = null
    private lateinit var datePickerDialog: MaterialDatePicker<Long>
    private lateinit var binding: FragmentOnScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnScreenBinding.inflate(inflater)
        initLce(binding.loadingView, binding.recyclerView, binding.errorView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar(binding.toolbar)
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = onScreenAdapter
        binding.fab.setOnClickListener { displayDatePicker() }
        emptinessHelper.init(binding.recyclerView, binding.emptyView)

        onStates(cinemaViewModel) { state ->
            when (state) {
                is CinemaState.Init -> cinemaViewModel.loadPromotional()
                is CinemaState.Loading -> showLoading()
                is CinemaState.OnScreen -> {
                    dateParams = state.dateParams
                    setTitle(dateParams?.currentDate?.longFormatToUi())
                    binding.fab.isVisible =
                        null != dateParams?.minimumDate && null != dateParams?.maximumDate
                    updateMovies(state.movies)
                    showContent()
                }
                is CinemaState.Error -> {
                    showError(state.error) { cinemaViewModel.loadMovies() }
                    setTitle(R.string.app_name)
                }
            }
        }

        onEvents(cinemaViewModel) { event ->
            when (event) {
                is CinemaEvent.Promotional -> requireActivity().start<PromotionalActivity> {
                    putExtra(PromotionalActivity.COVER_URL, event.url)
                }
            }
        }

        savedInstanceState ?: cinemaViewModel.loadMovies()
    }

    private fun updateMovies(movies: List<Movie>) {
        val adapters = movies.map { MovieAdapter(it, ::onMovieSelected) }
        onScreenAdapter.updateItems(adapters)
    }

    private fun onMovieSelected(movie: Movie) {
        cinemaViewModel.loadMovieDetails(movie)
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
                    cinemaViewModel.selectDate(pickedDate)
                    cinemaViewModel.loadMovies()
                }
                datePickerDialog.show(childFragmentManager, null)
            }
        }
    }

    fun isTodayDisplayed(): Boolean = dateParams?.currentDate?.isToday() == true
}
