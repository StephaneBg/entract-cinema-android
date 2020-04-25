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
import com.cinema.entract.app.R
import com.cinema.entract.app.ui.CinemaActivity
import com.cinema.entract.core.ext.start
import com.cinema.entract.core.ui.BaseActivity
import io.uniflow.androidx.flow.onStates
import io.uniflow.core.flow.data.UIState
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartupActivity : BaseActivity() {

    private val viewModel by viewModel<StartupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_startup)

        onStates(viewModel) { state ->
            when (state) {
                is UIState.Success,
                is UIState.Failed -> {
                    start<CinemaActivity>()
                    finish()
                }
            }
        }

        viewModel.prefetch()
    }
}
