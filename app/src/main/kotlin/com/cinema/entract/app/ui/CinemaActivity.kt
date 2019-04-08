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

package com.cinema.entract.app.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.information.InformationFragment
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.app.ui.settings.SettingsFragment
import com.cinema.entract.core.ext.isDarkTheme
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ext.replaceFragment
import com.cinema.entract.core.ext.setLightStatusBar
import com.cinema.entract.core.ui.BaseActivity
import com.cinema.entract.core.ui.scrollToTop
import com.cinema.entract.data.interactor.CinemaUseCase
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.find
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.threeten.bp.LocalDate


class CinemaActivity : BaseActivity() {

    private val cinemaViewModel by viewModel<CinemaViewModel>()
    private val navViewModel by viewModel<NavigationViewModel>()
    private val tagViewModel by viewModel<TagViewModel>()
    private val useCase by inject<CinemaUseCase>()

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(useCase.getThemeMode())

        setContentView(R.layout.activity_cinema)
        if (!isDarkTheme()) setLightStatusBar()
        initBottomNavigation()

        observe(navViewModel.state, ::manageNavigation)

        savedInstanceState ?: showFragment(OnScreenFragment.newInstance())
    }

    private fun manageNavigation(state: NavState?) {
        when (state) {
            NavState.Home -> bottomNav.selectedItemId = R.id.on_screen
            NavState.OnScreen -> when (val fragment = displayedFragment()) {
                is OnScreenFragment -> {
                    if (!fragment.scrollToTop() && !fragment.isTodayDisplayed())
                        cinemaViewModel.process(CinemaAction.RefreshMovies(LocalDate.now()))
                }
                else -> {
                    supportFragmentManager.popBackStack()
                    showFragment(OnScreenFragment.newInstance())
                }
            }
            NavState.Schedule -> when (val fragment = displayedFragment()) {
                is ScheduleFragment -> fragment.scrollToTop()
                else -> {
                    supportFragmentManager.popBackStack()
                    showFragment(ScheduleFragment.newInstance())
                    tagViewModel.tag(TagAction.Schedule)
                }
            }
            NavState.Details -> showFragment(DetailsFragment.newInstance(), true)
            NavState.Info -> showFragment(InformationFragment.newInstance())
            NavState.Settings -> showFragment(SettingsFragment.newInstance())
            NavState.Back -> when (val fragment = displayedFragment()) {
                is OnScreenFragment -> {
                    if (fragment.isTodayDisplayed()) super.onBackPressed()
                    else cinemaViewModel.process(CinemaAction.RefreshMovies(LocalDate.now()))
                }
                is DetailsFragment -> supportFragmentManager.popBackStack()
                else -> {
                    supportFragmentManager.popBackStack()
                    showFragment(OnScreenFragment.newInstance())
                }
            }
        }
    }

    override fun onBackPressed() {
        val origin = when (displayedFragment()) {
            is DetailsFragment -> NavOrigin.DETAILS
            is ScheduleFragment -> NavOrigin.SCHEDULE
            is OnScreenFragment -> NavOrigin.ON_SCREEN
            is InformationFragment -> NavOrigin.INFO
            is SettingsFragment -> NavOrigin.SETTINGS
            else -> error("Incorrect origin fragment")
        }
        navViewModel.process(NavAction.Back(origin))
    }

    private fun initBottomNavigation() {
        bottomNav = find(R.id.bottomNavigation)
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.on_screen -> {
                    navViewModel.process(NavAction.OnScreen(NavOrigin.ON_SCREEN))
                    true
                }
                R.id.schedule -> {
                    navViewModel.process(NavAction.Schedule(NavOrigin.SCHEDULE))
                    true
                }
                bottomNav.selectedItemId -> true
                R.id.information -> {
                    navViewModel.process(NavAction.Info)
                    true
                }
                R.id.settings -> {
                    navViewModel.process(NavAction.Settings)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment, addToBackStack: Boolean = false) {
        replaceFragment(
            R.id.mainContainer,
            fragment,
            addToBackStack,
            R.anim.fade_in,
            R.anim.fade_out
        )
    }

    private fun displayedFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.mainContainer)
}
