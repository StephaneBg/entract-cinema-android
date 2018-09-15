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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.model.ScheduleEntry
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.base.BaseLceFragment
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import com.cinema.entract.app.widget.EmptyRecyclerView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ScheduleFragment : BaseLceFragment<EmptyRecyclerView>() {

    private val viewModel by sharedViewModel<CinemaViewModel>()
    private val scheduleAdapter = ScheduleAdapter {}

    private lateinit var empty: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_schedule, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        empty = find(R.id.emptyView)
        with(contentView) {
            layoutManager = LinearLayoutManager(activity)
            adapter = scheduleAdapter
            emptyView = empty
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        observe(viewModel.getSchedule(), ::displaySchedule)
    }

    override fun showLoading() {
        empty.isVisible = false
        super.showLoading()
    }

    override fun showError(throwable: Throwable?, action: () -> Unit) {
        empty.isVisible = false
        super.showError(throwable, action)
    }

    private fun displaySchedule(resource: Resource<List<ScheduleEntry>>?) {
        when (resource) {
            is Loading -> showLoading()
            is Success -> {
                scheduleAdapter.updateSchedule(resource.data ?: emptyList())
                showContent()
            }
            is Error -> showError(resource.error) { viewModel.retrieveSchedule() }
        }

    }

    companion object {
        fun newInstance(): ScheduleFragment = ScheduleFragment()
    }
}