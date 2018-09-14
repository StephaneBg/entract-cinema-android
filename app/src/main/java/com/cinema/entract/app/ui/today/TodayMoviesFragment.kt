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

package com.cinema.entract.app.ui.today

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
import com.cinema.entract.app.widget.EmptyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TodayMoviesFragment : BaseLceFragment<EmptyRecyclerView>() {

    private val viewModel by sharedViewModel<TodayViewModel>()
    private val todayAdapter = TodayMoviesAdapter(::onMovieSelected)

    private lateinit var datePicker: MaterialCalendarView
    private lateinit var alertDialog: AlertDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_today_movies, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(contentView.find<EmptyRecyclerView>(R.id.recyclerView)) {
            layoutManager = LinearLayoutManager(activity)
            adapter = todayAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            emptyView = contentView.find(R.id.emptyView)
            setHasFixedSize(true)
        }

        find<FloatingActionButton>(R.id.fab).setOnClickListener { displayDatePicker() }

        observe(viewModel.getMovies(), ::displayMovies)
        observe(viewModel.getDate()) { find<TextView>(R.id.date).text = it }
    }

    private fun displayMovies(resource: Resource<List<Movie>>?) {
        when (resource) {
            is Loading -> showLoading()
            is Success -> {
                todayAdapter.updateMovies(resource.data ?: emptyList())
                showContent()
            }
            is Error -> showError(resource.error) { viewModel.getMovies() }
        }
    }

    private fun onMovieSelected(movie: Movie) {
        viewModel.selectedMovie = movie
        requireActivity().replaceFragment(
            R.id.mainContainer,
            TodayDetailsFragment.newInstance(),
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
        fun newInstance(): TodayMoviesFragment = TodayMoviesFragment()
    }
}