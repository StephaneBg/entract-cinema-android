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

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ext.replaceFragment
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.ui.Error
import com.cinema.entract.core.ui.Loading
import com.cinema.entract.core.ui.State
import com.cinema.entract.core.ui.Success
import com.cinema.entract.core.widget.EmptynessLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.threeten.bp.LocalDate

class OnScreenFragment : BaseLceFragment<EmptynessLayout>() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private lateinit var onScreenAdapter: OnScreenAdapter

    private lateinit var alertDialog: AlertDialog
    private lateinit var fab: FloatingActionButton
    private lateinit var date: TextView

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
            recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            recyclerView.setHasFixedSize(true)
            setAdapter(onScreenAdapter)
        }

        fab = find<FloatingActionButton>(R.id.fab).apply {
            isVisible = null != cinemaViewModel.getDateRange()
            setOnClickListener { displayDatePicker() }
        }

        date = find(R.id.date)
    }

    override fun onStart() {
        super.onStart()
        observe(cinemaViewModel.getCurrentDate()) { date.text = it }
        observe(cinemaViewModel.getOnScreenState(), ::manageState)
    }

    private fun manageState(state: State<List<Movie>>?) {
        when (state) {
            null -> Unit
            is Loading -> showLoading()
            is Success -> updateMovies(state.data)
            is Error -> manageError(state.error)
        }
    }

    private fun updateMovies(movies: List<Movie>) {
        contentView.recyclerView.layoutAnimation = animController
        onScreenAdapter.updateMovies(movies)
        showContent()
        contentView.recyclerView.scheduleLayoutAnimation()
    }

    private fun manageError(exception: Throwable?) {
        showError(exception) { cinemaViewModel.retrieveMovies() }
        date.text = getString(R.string.app_name)
    }

    private fun onMovieSelected(movie: Movie) {
        cinemaViewModel.selectMovie(movie)
        requireActivity().replaceFragment(
            R.id.mainContainer,
            DetailsFragment.newInstance(),
            true,
            R.anim.fade_in,
            R.anim.fade_out
        )
    }

    private fun displayDatePicker() {
        cinemaViewModel.getDateRange()?.let {
            val calendarView = CalendarView(requireContext()).apply {
                minDate = it.minimumDate
                maxDate = it.maximumDate
                setOnDateChangeListener { _, year, month, dayOfMonth ->
                    val date = LocalDate.of(year, month + 1, dayOfMonth)
                    cinemaViewModel.retrieveMovies(date)
                    alertDialog.dismiss()
                }
            }

            alertDialog = AlertDialog.Builder(requireContext())
                .setView(calendarView)
                .create()
            alertDialog.show()
        }
    }

    companion object {
        fun newInstance(): OnScreenFragment = OnScreenFragment()
    }
}