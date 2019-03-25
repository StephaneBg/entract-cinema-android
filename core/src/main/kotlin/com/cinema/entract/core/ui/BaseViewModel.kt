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

package com.cinema.entract.core.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch

open class BaseViewModel<Action, State> : ViewModel() {

    private val actions = Channel<Action>()
    protected val state = MutableLiveData<State>()
    val observableState: LiveData<State> = state

    init {
        viewModelScope.launch {
            actions.consumeEach {
                bindActions(it)
            }
        }
    }

    protected fun <T> launchAsync(
        tryBlock: suspend () -> T,
        catchBlock: (Throwable) -> T
    ) {
        viewModelScope.launch {
            try {
                tryBlock()
            } catch (throwable: Throwable) {
                if (throwable !is CancellationException) catchBlock(throwable)
            }
        }
    }

    fun dispatch(action: Action) {
        viewModelScope.launch { actions.send(action) }
    }

    protected open suspend fun bindActions(action: Action) = Unit
}
