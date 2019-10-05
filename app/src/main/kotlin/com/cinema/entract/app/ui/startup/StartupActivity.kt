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

package com.cinema.entract.app.ui.startup

import android.os.Bundle
import com.cinema.entract.app.databinding.ActivityStartupBinding
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.core.ext.observe
import com.cinema.entract.core.ui.BaseActivity
import org.jetbrains.anko.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartupActivity : BaseActivity() {

    private val viewModel by viewModel<StartupViewModel>()
    private lateinit var binding: ActivityStartupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observe(viewModel.state, ::renderState)
        viewModel.prefetch()
    }

    override fun onStart() {
        super.onStart()
        binding.progress.show()
    }

    private fun renderState(state: StartupState?) = when {
        null == state || state.isLoading -> Unit
        else -> {
            startActivity<CinemaActivity>()
            finish()
        }
    }
}
