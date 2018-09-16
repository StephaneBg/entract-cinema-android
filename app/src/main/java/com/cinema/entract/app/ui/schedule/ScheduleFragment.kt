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

package com.cinema.entract.app.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.base.BaseLceFragment
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.app.ui.movies.MoviesViewModel
import com.cinema.entract.app.widget.EmptynessLayout
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate

class ScheduleFragment : BaseLceFragment<EmptynessLayout>() {

    private val moviesViewModel by sharedViewModel<MoviesViewModel>()
    private val scheduleViewModel by viewModel<ScheduleViewModel>()
    private val scheduleAdapter = ScheduleAdapter(::handleSelection)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(contentView) {
            recyclerView.layoutManager = LinearLayoutManager(activity)
            recyclerView.addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            setAdapter(scheduleAdapter)
        }

        observe(scheduleViewModel.getSchedule(), ::displaySchedule)
    }

    private fun displaySchedule(resource: Resource<List<ScheduleEntry>>?) {
        when (resource) {
            is Loading -> showLoading()
            is Success -> {
                scheduleAdapter.updateSchedule(resource.data ?: emptyList())
                showContent()
            }
            is Error -> showError(resource.error) { scheduleViewModel.retrieveSchedule() }
        }

    }

    private fun handleSelection(date: LocalDate) {
        moviesViewModel.retrieveMovies(date)
        (requireActivity() as CinemaActivity).selectMovies()
    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}