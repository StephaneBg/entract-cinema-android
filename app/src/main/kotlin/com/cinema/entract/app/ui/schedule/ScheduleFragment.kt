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
import androidx.annotation.IdRes
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.FragmentScheduleBinding
import com.cinema.entract.app.model.DayHeader
import com.cinema.entract.app.model.MovieEntry
import com.cinema.entract.app.model.WeekHeader
import com.cinema.entract.app.ui.*
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.widget.GenericRecyclerViewAdapter
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScheduleFragment : BaseLceFragment() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
    private val tagViewModel by sharedViewModel<TagViewModel>()
    private val scheduleAdapter = GenericRecyclerViewAdapter()
    private lateinit var binding: FragmentScheduleBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScheduleBinding.inflate(inflater)
        initLce(binding.loadingView, binding.contentView, binding.errorView)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(R.string.schedule_title)

        with(binding.contentView) {
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.addItemDecoration(ScheduleDecoration(context))
            setAdapter(scheduleAdapter)
        }

        observe(cinemaViewModel.state, ::renderState)

        savedInstanceState ?: cinemaViewModel.process(CinemaAction.RefreshSchedule)

        tagViewModel.tag(TagAction.Schedule)
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
                cinemaViewModel.process(CinemaAction.RefreshSchedule)
            }
        }
    }

    private fun handleSelection(cinemaAction: CinemaAction, @IdRes destination: Int) {
        cinemaViewModel.process(cinemaAction)
        findNavController().navigate(destination)
    }

    fun scrollToTop() = if (binding.contentView.isScrolled()) {
        binding.contentView.scrollToTop()
        true
    } else {
        false
    }
}
