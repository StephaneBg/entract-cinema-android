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
import androidx.core.view.isVisible
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.find
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.ext.observe
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.app.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DetailsFragment : BaseFragment() {

    private val viewModel by sharedViewModel<CinemaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_details, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(viewModel.getSelectedMovie(), ::displayMovie)
    }

    private fun displayMovie(movie: Movie?) {
        movie?.let {
            find<ImageView>(R.id.cover).load(movie.coverUrl)
            find<TextView>(R.id.title).text = movie.title
            find<TextView>(R.id.director).text = movie.director
            movie.cast.apply {
                val view = find<TextView>(R.id.cast)
                if (isEmpty()) view.isVisible = false
                else view.text = this
            }
            find<TextView>(R.id.year).text = movie.yearOfProduction
            find<TextView>(R.id.duration).text = movie.duration
            find<TextView>(R.id.genre).text = movie.genre
            find<TextView>(R.id.synopsis).text = movie.synopsis
        }
    }

    companion object {
        fun newInstance() = DetailsFragment()
    }
}