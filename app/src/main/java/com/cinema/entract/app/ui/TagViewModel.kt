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

package com.cinema.entract.app.ui

import com.cinema.entract.core.ui.ScopedViewModel
import com.cinema.entract.data.interactor.TagUseCase

class TagViewModel(private val useCase: TagUseCase) : ScopedViewModel<TagAction>() {

    override fun manageAction(action: TagAction) = launchAsync(
        {
            when (action) {
                TagAction.Schedule -> useCase.tagSchedule()
                TagAction.Event -> useCase.tagEvent()
                is TagAction.Details -> useCase.tagDetails(action.sessionId)
                is TagAction.Calendar -> useCase.tagCalendar(action.sessionId)
            }
        },
        {}
    )
}

sealed class TagAction {
    object Schedule : TagAction()
    object Event : TagAction()
    data class Details(val sessionId: String) : TagAction()
    data class Calendar(val sessionId: String) : TagAction()
}
