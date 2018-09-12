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
import android.widget.TextView
import com.bumptech.glide.Glide
import com.cinema.entract.ui.R
import com.cinema.entract.ui.view.base.BaseFragment
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TodayDetailsFragment : BaseFragment() {

    private val viewModel by sharedViewModel<TodayViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_today_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.selectedMovie?.let {
            with(view) {
                Glide.with(context).load(it.coverUrl).into(find(R.id.cover))
                find<TextView>(R.id.title).text = it.title
                find<TextView>(R.id.director).text = it.director
                find<TextView>(R.id.cast).text = it.cast
                find<TextView>(R.id.year).text = it.yearOfProduction
                find<TextView>(R.id.duration).text = it.duration
                find<TextView>(R.id.genre).text = it.genre
                find<TextView>(R.id.synopsis).text = it.synopsis
            }
        }
    }

    companion object {
        fun newInstance() = TodayDetailsFragment()
    }
}