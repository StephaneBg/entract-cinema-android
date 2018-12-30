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
import androidx.fragment.app.Fragment
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.details.DetailsFragment
import com.cinema.entract.app.ui.event.EventDialogFragment
import com.cinema.entract.app.ui.information.InformationFragment
import com.cinema.entract.app.ui.onscreen.OnScreenFragment
import com.cinema.entract.app.ui.schedule.ScheduleFragment
import com.cinema.entract.app.ui.settings.SettingsFragment
import com.cinema.entract.app.ui.settings.SettingsViewModel
import com.cinema.entract.core.ext.addFragment
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ext.replaceFragment
import com.cinema.entract.core.ui.BaseActivity
import com.cinema.entract.core.ui.Event
import com.cinema.entract.core.views.bindView
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.viewModel

class CinemaActivity : BaseActivity() {

    private val cinemaViewModel by viewModel<CinemaViewModel>()
    private val tagViewModel by viewModel<TagViewModel>()
    private val settingsViewModel by viewModel<SettingsViewModel>()

    private val bottomNav by bindView<BottomNavigationView>(R.id.bottomNavigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(if (settingsViewModel.isDarkMode()) R.style.Theme_Cinema_Dark else R.style.Theme_Cinema_Light)
        setContentView(R.layout.activity_cinema)
        initBottomNavigation()

        savedInstanceState ?: addFragment(R.id.mainContainer, OnScreenFragment.newInstance())
        observe(cinemaViewModel.getEventUrl(), ::handleEvent)
    }

    private fun handleEvent(event: Event<String>?) {
        when (val url = event?.getContent()) {
            null, "" -> Unit
            else -> EventDialogFragment.show(supportFragmentManager, url)
        }
    }

    fun selectOnScreen() {
        bottomNav.selectedItemId = R.id.on_screen
    }

    override fun onBackPressed() {
        if (displayedFragment() is OnScreenFragment) {
            if (cinemaViewModel.isTodayDisplayed()) {
                super.onBackPressed()
            } else {
                cinemaViewModel.retrieveTodayMovies()
            }
        } else {
            selectOnScreen()
        }
    }

    private fun initBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.on_screen -> handleOnScreen()
                R.id.schedule -> handleSchedule()
                R.id.information -> handleInformation()
                R.id.settings -> handleSettings()
                else -> false
            }
        }
    }

    private fun handleOnScreen(): Boolean =
        when (val fragment = displayedFragment()) {
            is DetailsFragment -> {
                supportFragmentManager.popBackStack()
                true
            }
            is OnScreenFragment -> {
                if (fragment.isScrolled()) fragment.scrollToTop()
                else if (!cinemaViewModel.isTodayDisplayed()) cinemaViewModel.retrieveTodayMovies()
                true
            }
            else -> {
                replaceFragment(
                    R.id.mainContainer, OnScreenFragment.newInstance(),
                    false,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                true
            }
        }

    private fun handleSchedule(): Boolean =
        when (val fragment = displayedFragment()) {
            is ScheduleFragment -> {
                fragment.scrollToTop()
                false
            }
            else -> {
                supportFragmentManager.popBackStack()
                replaceFragment(
                    R.id.mainContainer, ScheduleFragment.newInstance(),
                    false,
                    R.anim.fade_in,
                    R.anim.fade_out
                )
                tagViewModel.tagSchedule()
                true
            }
        }

    private fun handleInformation(): Boolean {
        supportFragmentManager.popBackStack()
        replaceFragment(
            R.id.mainContainer, InformationFragment.newInstance(),
            false,
            R.anim.fade_in,
            R.anim.fade_out
        )
        return true
    }

    private fun handleSettings(): Boolean {
        supportFragmentManager.popBackStack()
        replaceFragment(
            R.id.mainContainer, SettingsFragment.newInstance(),
            false,
            R.anim.fade_in,
            R.anim.fade_out
        )
        return true
    }

    private fun displayedFragment(): Fragment? =
        supportFragmentManager.findFragmentById(R.id.mainContainer)
}
