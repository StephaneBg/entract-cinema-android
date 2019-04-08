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

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.CinemaAction
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.app.ui.CinemaViewModel
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ui.BaseFragment
import com.cinema.entract.core.widget.AppBarNestedScrollViewOnScrollListener
import com.cinema.entract.data.interactor.CinemaUseCase
import com.google.android.material.switchmaterial.SwitchMaterial
import org.jetbrains.anko.startActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import kotlin.math.max

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

        find<NestedScrollView>(R.id.scrollView).setOnScrollChangeListener(
            AppBarNestedScrollViewOnScrollListener(find(R.id.appBar))
        )

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

        find<TextView>(R.id.themeMode).text = getModeText()

        find<Button>(R.id.themeModeAction).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setSingleChoiceItems(getChoices(), getCurrentChoice()) { _, which ->
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

    private fun getChoices(): Array<String> {
        val choices = mutableListOf<String>()
        if (isPieOrAbove()) choices += getString(R.string.settings_system_mode)
        choices += getString(R.string.settings_light_mode)
        choices += getString(R.string.settings_dark_mode)
        choices += getString(R.string.settings_battery_mode)
        return choices.toTypedArray()
    }

    private fun getCurrentChoice(): Int {
        val mode = useCase.getThemeMode()
        return if (isPieOrAbove()) {
            if (-1 == mode) 0 else mode
        } else {
            max(0, mode - 1)
        }
    }

    private fun saveSelectedChoice(which: Int) {
        val mode = if (isPieOrAbove()) {
            if (0 == which) -1 else which
        } else {
            which + 1
        }
        useCase.setThemeMode(mode)
    }

    private fun getModeText(): String {
        var mode = useCase.getThemeMode()
        mode = if (!isPieOrAbove() && AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM == mode)
            AppCompatDelegate.MODE_NIGHT_NO else mode
        return when (mode) {
            AppCompatDelegate.MODE_NIGHT_NO -> getString(R.string.settings_light_mode)
            AppCompatDelegate.MODE_NIGHT_YES -> getString(R.string.settings_dark_mode)
            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> getString(R.string.settings_battery_mode)
            else -> getString(R.string.settings_system_mode)
        }

    }

    private fun isPieOrAbove(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}