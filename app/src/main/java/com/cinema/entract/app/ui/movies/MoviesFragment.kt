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

package com.cinema.entract.app.ui.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.ext.replaceFragment
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.base.*
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.settings.SettingsViewModel
import com.cinema.entract.app.widget.EmptynessLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : BaseLceFragment<EmptynessLayout>() {

    private val moviesViewModel by viewModel<MoviesViewModel>()
    private val prefsViewModel by viewModel<SettingsViewModel>()
    private lateinit var moviesAdapter: MoviesAdapter

    private lateinit var datePicker: MaterialCalendarView
    private lateinit var alertDialog: AlertDialog
    private lateinit var fab: FloatingActionButton
    private lateinit var date: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        moviesAdapter = MoviesAdapter(prefsViewModel.canDisplayMedia(), ::onMovieSelected)
        with(contentView) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            recyclerView.setHasFixedSize(true)
            setAdapter(moviesAdapter)
        }

        date = find(R.id.date)
        fab = find(R.id.fab)
        fab.setOnClickListener { displayDatePicker() }

        observe(moviesViewModel.getState(), ::manageResource)
    }

    private fun manageResource(state: State<OnScreen>?) {
        when (state) {
            is Loading -> showLoading()
            is Success -> {
                moviesAdapter.updateMovies(state.data?.getMovies() ?: emptyList())
                date.text = state.data?.getDate() ?: getString(R.string.app_name)
                showContent()
            }
            is Error -> {
                showError(state.error) { moviesViewModel.retrieveMovies() }
                date.text = getString(R.string.app_name)
            }
        }
    }

    private fun onMovieSelected(movie: Movie, cover: ImageView) {
        val fragment = prepareTransition(movie, cover)
        requireActivity().replaceFragment(
            R.id.mainContainer,
            fragment,
            cover,
            true
        )
    }

    private fun prepareTransition(movie: Movie, cover: ImageView): Fragment {
        val context = requireContext()
        sharedElementReturnTransition =
                TransitionInflater.from(context).inflateTransition(R.transition.cover_transition)
        exitTransition = TransitionInflater.from(context)
            .inflateTransition(android.R.transition.no_transition)

        return DetailsFragment.newInstance(movie, cover.transitionName).apply {
            sharedElementEnterTransition = TransitionInflater.from(context)
                .inflateTransition(R.transition.cover_transition)
            enterTransition = TransitionInflater.from(context)
                .inflateTransition(android.R.transition.no_transition)
        }
    }

    private fun displayDatePicker() {
        datePicker = MaterialCalendarView(context)
        datePicker.setOnDateChangedListener { _, day, _ ->
            moviesViewModel.retrieveMovies(day.date)
            alertDialog.dismiss()
        }
        moviesViewModel.dateRange?.let {
            datePicker.state().edit()
                .setMinimumDate(it.minimumDate)
                .setMaximumDate(it.maximumDate)
                .commit()
        }

        alertDialog = AlertDialog.Builder(requireContext())
            .setView(datePicker)
            .setNegativeButton(android.R.string.cancel, null)
            .create()
        alertDialog.show()
    }

    companion object {
        fun newInstance(): MoviesFragment = MoviesFragment()
    }
}