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

package com.cinema.entract.app.ui.event

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.cinema.entract.app.R
import com.cinema.entract.app.ext.load
import com.cinema.entract.app.ui.TagViewModel
import com.cinema.entract.core.ext.find
import com.google.android.material.button.MaterialButton
import org.koin.androidx.viewmodel.ext.sharedViewModel

class EventDialogFragment : DialogFragment() {

    private val tagViewModel by sharedViewModel<TagViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tagViewModel.tagEvent()

        find<ImageView>(R.id.cover).load(
            arguments?.getString(COVER_URL) ?: error("Use show()!")
        )

        find<MaterialButton>(R.id.close).setOnClickListener {
            dialog?.dismiss()
        }
    }

    companion object {
        private const val COVER_URL = "COVER_URL"

        fun show(fragmentManager: FragmentManager, url: String) = EventDialogFragment().apply {
            arguments = bundleOf(COVER_URL to url)
        }.show(fragmentManager, url)
    }
}