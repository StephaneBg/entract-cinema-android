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
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.ext.replaceFragment
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.base.BaseLceFragment
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.widget.EmptynessLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseLceFragment<EmptynessLayout>() {

    private val moviesViewModel by sharedViewModel<MoviesViewModel>()
    private val moviesAdapter = MoviesAdapter(::onMovieSelected)

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

        with(contentView) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            setAdapter(moviesAdapter)
        }

        date = find(R.id.date)
        fab = find(R.id.fab)
        fab.setOnClickListener { displayDatePicker() }

        observe(moviesViewModel.getMovies(), ::manageResource)
        observe(moviesViewModel.getDate(), ::manageDate)
    }

    private fun manageResource(resource: Resource<List<Movie>>?) {
        when (resource) {
            is Loading -> showLoading()
            is Success -> {
                moviesAdapter.updateMovies(resource.data ?: emptyList())
                showContent()
            }
            is Error -> showError(resource.error) { moviesViewModel.retrieveMovies() }
        }
    }

    private fun manageDate(resource: Resource<String>?) {
        resource?.let {
            date.text = it.data ?: getString(R.string.app_name)
        }
    }

    private fun onMovieSelected(movie: Movie) {
        moviesViewModel.setSelectedMovie(movie)
        requireActivity().replaceFragment(
            R.id.mainContainer,
            DetailsFragment.newInstance(),
            true
        )
    }

    private fun displayDatePicker() {
        datePicker = MaterialCalendarView(context)
        datePicker.setOnDateChangedListener { _, day, _ ->
            moviesViewModel.retrieveMovies(day.date)
            alertDialog.dismiss()
        }
        moviesViewModel.getDateRange()?.let {
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