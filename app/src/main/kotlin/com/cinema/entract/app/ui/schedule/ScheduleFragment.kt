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

package com.cinema.entract.app.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinema.entract.app.R
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.WeekHeader
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
import com.cinema.entract.core.widget.GenericRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScheduleFragment : BaseLceFragment<EmptinessLayout>() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val navViewModel by sharedViewModel<NavigationViewModel>()
    private val scheduleAdapter = GenericRecyclerViewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(contentView) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addItemDecoration(ScheduleItemDecorator(context))
            recyclerView.addOnScrollListener(AppBarRecyclerViewOnScrollListener(find(R.id.appBar)))
            setAdapter(scheduleAdapter)
        }

        observe(cinemaViewModel.observableState, ::renderState)

        savedInstanceState ?: cinemaViewModel.dispatch(CinemaAction.LoadSchedule)
    }

    private fun renderState(state: CinemaState?) {
        when (state) {
            is CinemaState.Loading -> showLoading()
            is CinemaState.Schedule -> {
                val adapters = state.schedule.map {
                    when (it) {
                        is WeekHeader -> WeekHeaderAdapter(it)
                        is DayHeader -> DayHeaderAdapter(it, ::handleSelection)
                        is MovieEntry -> MovieAdapter(it, ::handleSelection)
                    }
                }
                scheduleAdapter.updateItems(adapters)
                showContent()
            }
            is CinemaState.Error -> showError(state.error) {
                cinemaViewModel.dispatch(CinemaAction.LoadSchedule)
            }
        }
    }

    private fun handleSelection(cinemaAction: CinemaAction, navAction: NavAction) {
        cinemaViewModel.dispatch(cinemaAction)
        navViewModel.dispatch(navAction)
    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}