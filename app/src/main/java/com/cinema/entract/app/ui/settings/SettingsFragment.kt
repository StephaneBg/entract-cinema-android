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

package com.cinema.entract.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ui.BaseFragment
import com.google.android.material.switchmaterial.SwitchMaterial
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.sharedViewModel
import org.koin.androidx.viewmodel.ext.viewModel

class SettingsFragment : BaseFragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = find<SwitchMaterial>(R.id.event)
        event.isChecked = settingsViewModel.isEventEnabled()
        event.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setEventPreference(isChecked)
        }

        val data = find<SwitchMaterial>(R.id.data)
        data.isChecked = settingsViewModel.isOnlyOnWifi()
        data.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setOnlyOnWifi(isChecked)
            cinemaViewModel.retrieveMovies()
        }

        val dark = find<SwitchMaterial>(R.id.dark)
        dark.isChecked = settingsViewModel.isDarkMode()
        dark.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setDarkMode(isChecked)
            requireActivity().run {
                startActivity<CinemaActivity>()
                finish()
            }
        }
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}