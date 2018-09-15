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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.ext.replaceFragment
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.base.BaseLceFragment
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.widget.EmptyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MoviesFragment : BaseLceFragment<EmptyRecyclerView>() {

    private val viewModel by sharedViewModel<CinemaViewModel>()
    private val moviesAdapter = MoviesAdapter(::onMovieSelected)

    private lateinit var datePicker: MaterialCalendarView
    private lateinit var alertDialog: AlertDialog
    private lateinit var empty: View
    private lateinit var fab: FloatingActionButton
    private lateinit var date: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty = find(R.id.emptyView)
        fab = find(R.id.fab)
        date = find(R.id.date)
        with(contentView) {
            layoutManager = LinearLayoutManager(activity)
            emptyView = empty
            adapter = moviesAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        fab.setOnClickListener { displayDatePicker() }

        observe(viewModel.getMovies(), ::manageResource)
        observe(viewModel.getDate(), ::manageDate)
    }

    override fun showContent() {
        fab.isVisible = true
        super.showContent()
    }

    override fun showLoading() {
        fab.isVisible = false
        empty.isVisible = false
        super.showLoading()
    }

    override fun showError(throwable: Throwable?, action: () -> Unit) {
        fab.isVisible = false
        empty.isVisible = false
        super.showError(throwable, action)
    }

    private fun manageResource(resource: Resource<List<Movie>>?) {
        when (resource) {
            is Loading -> showLoading()
            is Success -> {
                moviesAdapter.updateMovies(resource.data ?: emptyList())
                showContent()
            }
            is Error -> showError(resource.error) { viewModel.retrieveMovies() }
        }
    }

    private fun manageDate(resource: Resource<String>?) {
        resource?.let {
            date.text = it.data ?: getString(R.string.app_name)
        }
    }

    private fun onMovieSelected(movie: Movie) {
        viewModel.setSelectedMovie(movie)
        requireActivity().replaceFragment(
            R.id.mainContainer,
            DetailsFragment.newInstance(),
            true
        )
    }

    private fun displayDatePicker() {
        datePicker = MaterialCalendarView(context)
        datePicker.setOnDateChangedListener { _, day, _ ->
            viewModel.getMovies(day.date)
            alertDialog.dismiss()
        }
        viewModel.getDateRange()?.let {
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