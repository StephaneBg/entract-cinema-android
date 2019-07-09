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

package com.cinema.entract.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.CinemaAction
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ui.BaseFragment
import com.cinema.entract.core.utils.THEME_MODE_AUTO
import com.cinema.entract.core.utils.THEME_MODE_DARK
import com.cinema.entract.core.utils.THEME_MODE_LIGHT
import com.cinema.entract.data.interactor.CinemaUseCase
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class SettingsFragment : BaseFragment() {

    private val useCase by inject<CinemaUseCase>()
    private val cinemaViewModel by sharedViewModel<CinemaViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(R.string.settings_title)

        find<SwitchMaterial>(R.id.event).apply {
            isChecked = useCase.isEventEnabled()
            setOnCheckedChangeListener { _, isChecked ->
                useCase.setEventPreference(isChecked)
            }
        }

        find<SwitchMaterial>(R.id.data).apply {
            isChecked = useCase.isOnlyOnWifi()
            setOnCheckedChangeListener { _, isChecked ->
                useCase.setOnlyOnWifi(isChecked)
                cinemaViewModel.process(CinemaAction.LoadMovies())
            }
        }

        find<TextView>(R.id.themeMode).setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(R.string.settings_select_theme_mode)
                .setSingleChoiceItems(R.array.settings_theme_modes, getCurrentChoice()) { _, which ->
                    saveSelectedChoice(which)
                    requireActivity().run {
                        startActivity<CinemaActivity>()
                        finish()
                    }
                }
                .create()
                .show()
        }
    }

    private fun getCurrentChoice(): Int = when (useCase.getPrefThemeMode()) {
        THEME_MODE_LIGHT -> 0
        THEME_MODE_DARK -> 1
        else -> 2
    }

    private fun saveSelectedChoice(which: Int) {
        val mode = when (which) {
            0 -> THEME_MODE_LIGHT
            1 -> THEME_MODE_DARK
            else -> THEME_MODE_AUTO
        }
        useCase.setPrefThemeMode(mode)
    }
}
