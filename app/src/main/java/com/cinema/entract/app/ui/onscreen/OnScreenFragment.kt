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
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.cinema.entract.app.R
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.OnScreen
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.getDate
import com.cinema.entract.app.ui.getMovies
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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class OnScreenFragment : BaseLceFragment<EmptynessLayout>() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private lateinit var onScreenAdapter: OnScreenAdapter

    private lateinit var datePicker: MaterialCalendarView
    private lateinit var alertDialog: AlertDialog
    private lateinit var fab: FloatingActionButton
    private lateinit var date: TextView

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

        date = find(R.id.date)
        fab = find(R.id.fab)
        fab.setOnClickListener { displayDatePicker() }

        observe(cinemaViewModel.getOnScreenState(), ::manageState)
    }

    private fun manageState(state: State<OnScreen>?) {
        when (state) {
            is Loading -> showLoading()
            is Success -> {
                onScreenAdapter.updateMovies(state.data?.getMovies() ?: emptyList())
                date.text = state.data?.getDate() ?: getString(R.string.app_name)
                showContent()
            }
            is Error -> {
                showError(state.error) { cinemaViewModel.retrieveMovies() }
                date.text = getString(R.string.app_name)
            }
        }
    }

    private fun onMovieSelected(movie: Movie, cover: ImageView) {
        val fragment = prepareTransition(movie, cover)
        requireActivity().replaceFragment(R.id.mainContainer, fragment, cover, true)
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
            cinemaViewModel.retrieveMovies(day.date)
            alertDialog.dismiss()
        }
        cinemaViewModel.dateRange?.let {
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
        fun newInstance(): OnScreenFragment = OnScreenFragment()
    }
}