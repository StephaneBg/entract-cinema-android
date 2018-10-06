package com.cinema.entract.app.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import com.cinema.entract.app.R
import com.cinema.entract.core.ext.find
import com.cinema.entract.core.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = find<Switch>(R.id.event)
        event.isChecked = settingsViewModel.isEventEnabled()
        event.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setEventPreference(isChecked)
        }

        val data = find<Switch>(R.id.data)
        data.isChecked = settingsViewModel.isOnlyOnWifi()
        data.setOnCheckedChangeListener { _, isChecked ->
            settingsViewModel.setOnlyOnWifi(isChecked)
        }
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }
}