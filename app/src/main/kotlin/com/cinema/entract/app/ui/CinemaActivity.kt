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

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.ActivityCinemaBinding
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.core.ui.BaseActivity
import com.cinema.entract.data.interactor.CinemaUseCase
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate


class CinemaActivity : BaseActivity() {

    private val useCase by inject<CinemaUseCase>()
    private val cinemaViewModel by viewModel<CinemaViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(useCase.getThemeMode())

        val binding = ActivityCinemaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.navHost)

        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnNavigationItemReselectedListener {
            when (val fragment = getNavHostFragment()?.getDisplayedFragment()) {
                is OnScreenFragment -> manageOnScreen(fragment)
                is ScheduleFragment -> fragment.scrollToTop()
                is DetailsFragment -> navController.popBackStack()
            }
        }
    }

    private fun manageOnScreen(fragment: OnScreenFragment) {
        if (!fragment.isTodayDisplayed()) {
            cinemaViewModel.selectDate(LocalDate.now())
            cinemaViewModel.loadMovies()
        }
    }

    private fun getNavHostFragment(): NavHostFragment? =
        supportFragmentManager.findFragmentById(R.id.navHost) as? NavHostFragment

    private fun NavHostFragment.getDisplayedFragment(): Fragment? =
        childFragmentManager.primaryNavigationFragment
}
