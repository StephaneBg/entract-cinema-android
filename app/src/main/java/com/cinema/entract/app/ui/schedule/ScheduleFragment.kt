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
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseLceFragment
import com.cinema.entract.core.ui.Error
import com.cinema.entract.core.ui.Loading
import com.cinema.entract.core.ui.State
import com.cinema.entract.core.ui.Success
import com.cinema.entract.core.widget.AppBarRecyclerViewOnScrollListener
import com.cinema.entract.core.widget.EmptynessLayout
import org.koin.androidx.viewmodel.ext.sharedViewModel
import org.threeten.bp.LocalDate

class ScheduleFragment : BaseLceFragment<EmptynessLayout>() {

    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()
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
    }

    override fun onStart() {
        super.onStart()
        observe(cinemaViewModel.getScheduleState(), ::manageState)
    }

    fun scrollToTop() = contentView.recyclerView.smoothScrollToPosition(0)

    private fun manageState(state: State<List<ScheduleEntry>>?) {
        when (state) {
            null -> Unit
            is Loading -> showLoading()
            is Success -> {
                scheduleAdapter.updateSchedule(state.data)
                showContent()
            }
            is Error -> showError(state.error) { cinemaViewModel.retrieveSchedule() }
        }
    }

    private fun handleSelection(date: LocalDate) {
        cinemaViewModel.retrieveMovies(date)
        (activity as CinemaActivity).selectOnScreen()
    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}