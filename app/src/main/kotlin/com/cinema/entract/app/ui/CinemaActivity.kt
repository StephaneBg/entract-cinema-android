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

package com.cinema.entract.app.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.ActivityCinemaBinding
import com.cinema.entract.app.model.Movie
import com.cinema.entract.app.ui.details.DetailsActivity
import com.cinema.entract.app.ui.information.InformationFragment
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.app.ui.settings.SettingsFragment
import com.cinema.entract.core.ui.BaseActivity
import com.cinema.entract.data.interactor.CinemaUseCase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate


class CinemaActivity : BaseActivity() {

    private val useCase by inject<CinemaUseCase>()
    private val cinemaViewModel by viewModel<CinemaViewModel>()

    private lateinit var binding: ActivityCinemaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(useCase.getThemeMode())

        binding = ActivityCinemaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigation.setOnItemSelectedListener {
            displayFragment(it.itemId)
            true
        }
        binding.bottomNavigation.setOnItemReselectedListener {
            when (val fragment = supportFragmentManager.findFragmentById(R.id.container)) {
                is OnScreenFragment -> manageOnScreen(fragment)
                is ScheduleFragment -> fragment.scrollToTop()
            }
        }
        displayFragment(R.id.onScreen)
    }

    private fun displayFragment(id: Int) {
        val fragment = getFragmentOrCreate(id)
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    fun displayDetails(movie: Movie) {
        cinemaViewModel.selectMovie(movie)
        startActivity(Intent(this, DetailsActivity::class.java))
    }

    fun navigateTo(id: Int) {
        binding.bottomNavigation.selectedItemId = id
    }

    private fun manageOnScreen(fragment: OnScreenFragment) {
        if (!fragment.isTodayDisplayed()) {
            cinemaViewModel.selectDate(LocalDate.now())
            cinemaViewModel.loadMovies()
        }
    }

    private fun getFragmentOrCreate(id: Int): Fragment = when (id) {
        R.id.onScreen -> OnScreenFragment()
        R.id.schedule -> ScheduleFragment()
        R.id.information -> InformationFragment()
        R.id.settings -> SettingsFragment()
        else -> error("Incorrect id")
    }
}
