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

import com.cinema.entract.core.ui.BaseViewModel
import com.cinema.entract.data.interactor.CinemaUseCase
import io.uniflow.core.flow.ActionFlow
import io.uniflow.core.flow.data.UIState
import timber.log.Timber

class StartupViewModel(private val useCase: CinemaUseCase) : BaseViewModel() {

    override suspend fun onError(error: Exception, currentState: UIState, flow: ActionFlow) {
        Timber.e(error)
        action { setState(UIState.Failed()) }
    }

    fun prefetch() {
        action {
            useCase.getMovies()
            useCase.getDateRange()
            useCase.getEventUrl()
            setState(UIState.Success)
        }
    }
}
