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

package com.cinema.entract.app.ui.onscreen

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinema.entract.app.R
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaAction
import com.cinema.entract.app.ui.CinemaState
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.NavAction
import com.cinema.entract.app.ui.NavigationViewModel
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.widget.AppBarRecyclerViewOnScrollListener
import com.cinema.entract.core.widget.EmptinessLayout
import com.cinema.entract.data.ext.isToday
import com.cinema.entract.data.ext.longFormatToUi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.sharedViewModel
import org.threeten.bp.LocalDate

class OnScreenFragment : BaseLceFragment<EmptinessLayout>() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val navViewModel by sharedViewModel<NavigationViewModel>()
    private lateinit var onScreenAdapter: OnScreenAdapter
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var fab: FloatingActionButton
    private lateinit var date: TextView

    private var currentState: CinemaState.OnScreen? = null
    private val animController by lazy {
        AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_on_screen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onScreenAdapter = OnScreenAdapter(::onMovieSelected)
        with(contentView) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.setHasFixedSize(true)
            recyclerView.addOnScrollListener(AppBarRecyclerViewOnScrollListener(find(R.id.appBar)))
            setAdapter(onScreenAdapter)
        }

        fab = find<FloatingActionButton>(R.id.fab).apply {
            setOnClickListener { displayDatePicker() }
        }
        date = find(R.id.date)

        observe(cinemaViewModel.state, ::renderState)

        savedInstanceState ?: cinemaViewModel.dispatch(CinemaAction.LoadMovies())
    }

    private fun renderState(state: CinemaState?) {
        currentState = null
        when (state) {
            is CinemaState.Loading -> showLoading()
            is CinemaState.OnScreen -> {
                currentState = state
                date.text = state.date.longFormatToUi()
                fab.isVisible = null != state.dateRange
                updateMovies(state.movies)
                showContent()
            }
            is CinemaState.Error -> {
                date.text = getString(R.string.app_name)
                manageError(state.error)
            }
        }
    }

    private fun updateMovies(movies: List<Movie>) {
        contentView.recyclerView.layoutAnimation = animController
        onScreenAdapter.updateMovies(movies)
        showContent()
        contentView.recyclerView.scheduleLayoutAnimation()
    }

    private fun manageError(exception: Throwable?) {
        showError(exception) { cinemaViewModel.dispatch(CinemaAction.LoadMovies()) }
        date.text = getString(R.string.app_name)
    }

    private fun onMovieSelected(movie: Movie) {
        cinemaViewModel.dispatch(CinemaAction.LoadDetails(movie))
        navViewModel.dispatch(NavAction.Details)
    }

    private fun displayDatePicker() {
        val state = currentState
        state?.dateRange?.let {
            val date = state.date
            datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.Theme_Cinema_Dialog,
                { _, year, month, dayOfMonth ->
                    val pickedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    cinemaViewModel.dispatch(CinemaAction.LoadMovies(pickedDate))
                    datePickerDialog.dismiss()
                },
                date.year,
                date.monthValue - 1,
                date.dayOfMonth
            ).apply {
                datePicker.minDate = it.minimumDate
                datePicker.maxDate = it.maximumDate
                show()
            }
        }
    }

    fun isTodayDisplayed(): Boolean = currentState?.date?.isToday() == true

    companion object {
        fun newInstance(): OnScreenFragment = OnScreenFragment()
    }
}