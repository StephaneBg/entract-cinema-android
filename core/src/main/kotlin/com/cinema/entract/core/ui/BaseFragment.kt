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

package com.cinema.entract.core.ui

import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {

    fun setTitle(@StringRes title: Int) {
        setTitle(getString(title))
    }

    fun setTitle(title: String?) {
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.title = title
    }

    fun setToolbar(toolbar: Toolbar, showBack: Boolean = false) {
        (requireActivity() as AppCompatActivity).apply {
            setSupportActionBar(toolbar)
            if (showBack) supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }
}
