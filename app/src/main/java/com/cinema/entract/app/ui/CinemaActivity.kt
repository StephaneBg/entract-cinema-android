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

package com.cinema.entract.app.ui

import android.os.Bundle
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.addFragment
import com.cinema.entract.app.ext.replaceFragment
import com.cinema.entract.app.ui.base.BaseActivity
import com.cinema.entract.app.ui.details.MovieDetailsFragment
import com.cinema.entract.app.ui.today.TodayMoviesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class CinemaActivity : BaseActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cinema_entract)
        initWidgets()
        initBottomNavigation()

        savedInstanceState ?: addFragment(R.id.mainContainer, TodayMoviesFragment.newInstance())
    }

    private fun initWidgets() {
        bottomNav = find(R.id.bottomNavigation)
    }

    private fun initBottomNavigation() {
        bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.today -> handleToday()
                R.id.coming -> {
                    toast("Bientôt :)")
                    false
                }
                else -> false
            }
        }
    }

    private fun handleToday(): Boolean =
        when (supportFragmentManager.findFragmentById(R.id.mainContainer)) {
            is MovieDetailsFragment -> {
                supportFragmentManager.popBackStack()
                true
            }
            is TodayMoviesFragment -> false
            else -> {
                replaceFragment(R.id.mainContainer, TodayMoviesFragment.newInstance())
                true
            }
        }
}