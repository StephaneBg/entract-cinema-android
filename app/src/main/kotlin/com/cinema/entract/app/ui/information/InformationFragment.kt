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

package com.cinema.entract.app.ui.information

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import com.cinema.entract.app.R
import com.cinema.entract.app.databinding.FragmentInformationBinding
import com.cinema.entract.core.ui.BaseFragment
import org.jetbrains.anko.browse
import org.jetbrains.anko.toast
import timber.log.Timber

class InformationFragment : BaseFragment() {

    private lateinit var binding: FragmentInformationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInformationBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setTitle(R.string.information_title)

        binding.navigate.setOnClickListener {
            try {
                val gmmIntentUri = "geo:43.7700499,1.2948405?q=Grenade+Cinéma".toUri()
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                mapIntent.setPackage("com.google.android.apps.maps")
                startActivity(mapIntent)
            } catch (e: Exception) {
                Timber.e(e)
                requireContext().toast(R.string.error_general)
            }
        }
        binding.call.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+33561746234"))
                startActivity(intent)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        binding.website.setOnClickListener {
            requireContext().browse("http://www.grenadecinema.fr")
        }
        binding.facebook.setOnClickListener {
            requireContext().browse("https://www.facebook.com/profile.php?movieId=410990495629466")
        }
    }
}
