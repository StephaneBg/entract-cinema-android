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

package com.cinema.entract.ui.view.today

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cinema.entract.ui.R
import com.cinema.entract.ui.base.BaseLceFragment
import com.cinema.entract.ui.base.Error
import com.cinema.entract.ui.base.Loading
import com.cinema.entract.ui.base.Resource
import com.cinema.entract.ui.base.Success
import com.cinema.entract.ui.ext.find
import com.cinema.entract.ui.ext.observe
import com.cinema.entract.ui.model.Movie
import com.cinema.entract.ui.widget.EmptyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.jetbrains.anko.find
import org.koin.android.ext.android.inject

class TodayFragment : BaseLceFragment<EmptyRecyclerView>() {

    private val viewModel by inject<TodayViewModel>()
    private val todayAdapter = TodayAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_today, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(contentView.find<EmptyRecyclerView>(R.id.recyclerView)) {
            layoutManager = LinearLayoutManager(activity)
            adapter = todayAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            emptyView = contentView.find(R.id.emptyView)
            setHasFixedSize(true)
        }

        find<FloatingActionButton>(R.id.fab).isVisible = false

        observe(viewModel.getMovies(), ::displayMovies)
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

    companion object {
        fun newInstance(): TodayFragment = TodayFragment()
    }
}