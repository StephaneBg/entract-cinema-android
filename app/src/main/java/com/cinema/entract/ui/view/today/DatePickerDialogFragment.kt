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

package com.cinema.entract.ui.view.today

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.FragmentManager
import com.cinema.entract.ui.R
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class DatePickerDialogFragment : AppCompatDialogFragment() {

    private val viewModel by sharedViewModel<TodayViewModel>()

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val datePicker = LayoutInflater.from(context).inflate(
            R.layout.fragment_date_picker,
            null
        ) as MaterialCalendarView
        datePicker.setOnDateChangedListener { _, day, _ ->
            viewModel.getMovies(day.date)
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(datePicker)
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    companion object {
        private const val TAG = "DATE_PICKER_DIALOG"

        fun show(fragmentManager: FragmentManager) =
            DatePickerDialogFragment().show(fragmentManager, TAG)
    }
}