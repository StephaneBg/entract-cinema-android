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
import com.cinema.entract.app.NavAction
import com.cinema.entract.app.NavState
import com.cinema.entract.app.NavigationViewModel
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.CinemaAction
import com.cinema.entract.app.ui.CinemaState
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.widget.AppBarRecyclerViewOnScrollListener
import com.cinema.entract.core.widget.EmptynessLayout
import org.koin.androidx.viewmodel.ext.sharedViewModel

class ScheduleFragment : BaseLceFragment<EmptynessLayout>() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val navViewModel by sharedViewModel<NavigationViewModel>()
    private val scheduleAdapter = ScheduleAdapter(::handleSelection)

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

        observe(cinemaViewModel.state, ::renderState)
        observe(navViewModel.state, ::manageNavigation)

        savedInstanceState ?: cinemaViewModel.dispatch(CinemaAction.LoadSchedule)
    }

    private fun renderState(state: CinemaState?) {
        when (state) {
            is CinemaState.Loading -> showLoading()
            is CinemaState.Schedule -> {
                scheduleAdapter.updateSchedule(state.schedule)
                showContent()
            }
            is CinemaState.Error -> showError(state.error) {
                cinemaViewModel.dispatch(CinemaAction.LoadSchedule)
            }
        }
    }

    private fun manageNavigation(state: NavState?) {
        if (state is NavState.ScrollToTop) scrollToTop()
    }

    private fun handleSelection(cinemaAction: CinemaAction, navAction: NavAction) {
        cinemaViewModel.dispatch(cinemaAction)
        navViewModel.dispatch(navAction)
    }

    private fun scrollToTop() = contentView.recyclerView.smoothScrollToPosition(0)

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}