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

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

open class ScopedViewModel<Action> : ViewModel(), CoroutineScope {

    private val actions = Channel<Action>()
    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    init {
        launch { actions.consumeEach { manageAction(it) } }
    }

    fun <T> launchAsync(
        tryBlock: suspend () -> T,
        catchBlock: (Throwable) -> T
    ) {
        launch {
            try {
                tryBlock()
            } catch (e: Throwable) {
                if (e !is CancellationException) catchBlock(e)
            }
        }
    }

    fun perform(action: Action) = launch { actions.send(action) }

    open fun manageAction(action: Action) = Unit

    override fun onCleared() {
        super.onCleared()
        job.cancel()
        actions.close()
    }
}
