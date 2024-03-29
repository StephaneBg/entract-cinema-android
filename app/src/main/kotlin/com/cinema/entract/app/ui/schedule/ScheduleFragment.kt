/*
 * Copyright 2019 Stéphane Baiget
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
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.FragmentScheduleBinding
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.CinemaState
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.TagAction
import com.cinema.entract.app.ui.TagViewModel
import com.cinema.entract.core.ext.isScrolled
import com.cinema.entract.core.ext.scrollToTop
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.utils.EmptinessHelper
import com.cinema.entract.core.widget.GenericRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScheduleFragment : BaseLceFragment() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val tagViewModel by sharedViewModel<TagViewModel>()
    private val scheduleAdapter = GenericRecyclerViewAdapter()
    private val emptinessHelper = EmptinessHelper()
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScheduleBinding.inflate(inflater)
        initLce(binding.loadingView, binding.recyclerView, binding.errorView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar(binding.toolbar)
        binding.recyclerView.adapter = scheduleAdapter
        binding.recyclerView.addItemDecoration(ScheduleDecoration(requireContext()))
        emptinessHelper.init(binding.recyclerView, binding.emptyView)

        cinemaViewModel.observeStates(this) { state ->
            when (state) {
                is CinemaState.Loading -> showLoading()
                is CinemaState.Schedule -> {
                    val adapters = state.schedule.map {
                        when (it) {
                            is WeekHeader -> WeekHeaderAdapter(it)
                            is DayHeader -> DayHeaderAdapter(it) { date ->
                                cinemaViewModel.selectDate(date)
                                (activity as? CinemaActivity)?.navigateTo(R.id.onScreen)
                            }

                            is MovieEntry -> MovieAdapter(it) { movie ->
                                (activity as? CinemaActivity)?.displayDetails(movie)
                            }
                        }
                    }
                    scheduleAdapter.updateItems(adapters)
                    showContent()
                }

                is CinemaState.Error -> showError(state.error) {
                    cinemaViewModel.loadSchedule()
                }
            }
        }

        tagViewModel.tag(TagAction.Schedule)
    }

    override fun onResume() {
        super.onResume()
        cinemaViewModel.loadSchedule()
    }

    fun scrollToTop(): Boolean = if (binding.recyclerView.isScrolled()) {
        binding.recyclerView.scrollToTop()
        true
    } else {
        false
    }
}
