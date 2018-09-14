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

package com.cinema.entract.app.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.base.BaseLceFragment
import com.cinema.entract.app.ui.base.Error
import com.cinema.entract.app.ui.base.Loading
import com.cinema.entract.app.ui.base.Resource
import com.cinema.entract.app.ui.base.Success
import org.jetbrains.anko.find
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class MovieDetailsFragment : BaseLceFragment<View>() {

    private val viewModel by sharedViewModel<CinemaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_today_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.getSelectedMovie(), ::manageResource)
    }

    private fun manageResource(resource: Resource<Movie>?) {
        when (resource) {
            is Loading -> showLoading()
            is Success -> {
                resource.data?.let {
                    contentView.apply {
                        find<ImageView>(R.id.cover).load(it.coverUrl)
                        find<TextView>(R.id.title).text = it.title
                        find<TextView>(R.id.director).text = it.director
                        find<TextView>(R.id.cast).text = it.cast
                        find<TextView>(R.id.year).text = it.yearOfProduction
                        find<TextView>(R.id.duration).text = it.duration
                        find<TextView>(R.id.genre).text = it.genre
                        find<TextView>(R.id.synopsis).text = it.synopsis
                    }
                }
                showContent()
            }
            is Error -> showError(resource.error) { viewModel.getMovies() }
        }
    }

    companion object {
        fun newInstance() = MovieDetailsFragment()
    }
}