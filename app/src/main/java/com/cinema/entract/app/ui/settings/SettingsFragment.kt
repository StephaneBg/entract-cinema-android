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

    private val prefsViewModel by viewModel<SettingsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val event = find<Switch>(R.id.event)
        event.isChecked = prefsViewModel.isEventEnabled()
        event.setOnCheckedChangeListener { _, isChecked ->
            prefsViewModel.setEventPreference(isChecked)
        }

        val data = find<Switch>(R.id.data)
        data.isChecked = prefsViewModel.isOnlyOnWifi()
        data.setOnCheckedChangeListener { _, isChecked ->
            prefsViewModel.setOnlyOnWifi(isChecked)
        }
    }

    companion object {
        fun newInstance(): SettingsFragment = SettingsFragment()
    }

}