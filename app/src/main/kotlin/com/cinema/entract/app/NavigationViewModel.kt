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

package com.cinema.entract.app

import com.cinema.entract.core.ui.ScopedViewModel

class NavigationViewModel : ScopedViewModel<NavAction, NavState>() {

    private var currentState: NavState? = null

    override suspend fun bindActions(action: NavAction) {
        currentState = when (action) {
            is NavAction.OnScreen -> {
                when (action.origin) {
                    NavOrigin.BOTTOM_NAV -> NavState.OnScreen
                    NavOrigin.ON_SCREEN -> NavState.ScrollToTop
                    NavOrigin.SCHEDULE,
                    NavOrigin.DETAILS -> NavState.Home
                    NavOrigin.INFO,
                    NavOrigin.SETTINGS -> error("No hook from settings")
                }
            }
            is NavAction.Details -> NavState.Details
            is NavAction.Schedule -> {
                when (action.origin) {
                    NavOrigin.SCHEDULE -> NavState.ScrollToTop
                    else -> NavState.Schedule
                }
            }
            is NavAction.Info -> NavState.Info
            is NavAction.Settings -> NavState.Settings
            is NavAction.Back -> {
                when (action.origin) {
                    NavOrigin.BOTTOM_NAV -> NavState.ScrollToTop
                    NavOrigin.ON_SCREEN,
                    NavOrigin.DETAILS -> NavState.Back
                    NavOrigin.SCHEDULE,
                    NavOrigin.INFO,
                    NavOrigin.SETTINGS -> NavState.Home
                }
            }
        }
        innerState.postValue(currentState)
    }
}

sealed class NavState {
    object Home : NavState()
    object OnScreen : NavState()
    object Details : NavState()
    object Schedule : NavState()
    object Info : NavState()
    object Settings : NavState()
    object ScrollToTop : NavState()
    object Back : NavState()
}

sealed class NavAction {
    data class OnScreen(val origin: NavOrigin) : NavAction()
    object Details : NavAction()
    data class Schedule(val origin: NavOrigin) : NavAction()
    object Info : NavAction()
    object Settings : NavAction()
    data class Back(val origin: NavOrigin) : NavAction()
}

enum class NavOrigin {
    BOTTOM_NAV,
    ON_SCREEN,
    SCHEDULE,
    DETAILS,
    INFO,
    SETTINGS
}
