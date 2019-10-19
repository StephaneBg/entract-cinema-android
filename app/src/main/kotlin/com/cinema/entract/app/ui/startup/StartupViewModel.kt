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
import io.uniflow.core.flow.StateAction
import io.uniflow.core.flow.UIState
import timber.log.Timber

class StartupViewModel(private val useCase: CinemaUseCase) : BaseViewModel() {

    override suspend fun onError(error: Exception) {
        Timber.e(error)
        setState { UIState.Failed() }
    }

    fun prefetch(): StateAction = setState {
        useCase.getMovies()
        useCase.getDateRange()
        useCase.getEventUrl()
        UIState.Success
    }
}
